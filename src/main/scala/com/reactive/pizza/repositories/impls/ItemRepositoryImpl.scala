package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.ItemRepository
import com.reactive.pizza.repositories.persistences.tables.ItemDAO
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import com.reactive.pizza.utils.FailedCachingException
import play.api.cache.AsyncCacheApi

import javax.inject.{ Inject, Singleton }
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{ Failure, Success }

@Singleton
class ItemRepositoryImpl @Inject()(itemDAO: ItemDAO, dbComponent: MySqlDBComponent, cache: AsyncCacheApi)
  extends ItemRepository with ColumnCustomType {

  //--------------[ Properties ]---------------------------
  import dbComponent.mysqlDriver.api._
  private val db          = dbComponent.dbAction
  private implicit val ec = dbComponent.dbEC

  private val ALL_ITEM_KEY   = "all_item"
  private val CACHED_TIMEOUT = 24.hours

  //---------------[ Methods ]------------------------------
  override def findAll: Future[Seq[Item]] = {
    cache.get[Map[Item.Id, Item]](ALL_ITEM_KEY).flatMap {
      case Some(itemM) =>
        Future.successful(itemM.values.toSeq)
      case None        =>
        db run { itemDAO.items.result } map { rs =>
          val rMap = rs.map(rsi => rsi._1 -> rsi).toMap
          rs.map(itemDAO.apply(_, rMap))
        } andThen {
          case Success(items) =>
            val itemM = items.map(item => item.id -> item).toMap
            cache.set(ALL_ITEM_KEY, itemM, CACHED_TIMEOUT)
          case Failure(e)     =>
            throw new FailedCachingException(s"Set items cache is failed with error: ${e.getMessage}")
        }
    }
  }

  override def findById(id: Item.Id): Future[Option[Item]] = {
    cache.get[Map[Item.Id, Item]](ALL_ITEM_KEY).flatMap {
      case Some(itemM) =>
        Future.successful(itemM.get(id))
      case None        =>
        for {
          targetItemR <- db.run(itemDAO.items.filter(_.id === id).result.headOption)
          childItemRs <- targetItemR match {
            case Some(v) if v._8.nonEmpty =>
              db.run(itemDAO.items.filter(_.id inSet v._8).result)
            case _                        =>
              Future.successful(Nil)
          }
        } yield {
          val childItemRMap = childItemRs.map(r => r._1 -> r).toMap
          targetItemR.map(itemDAO.apply(_, childItemRMap))
        }
    }
  }

  override def filterByIds(ids: Seq[Item.Id]): Future[Seq[Item]] = {
    // Map[Item.Id, Map[Item.Id, Item]] -> Map[ComboItemId, Map[ChildItemId, ChildItem]]
    def filterComboItems(targetItemRs: Seq[itemDAO.ItemTupled]): Future[Map[Item.Id, Map[Item.Id, itemDAO.ItemTupled]]] = {
      val comboItemRs    = targetItemRs.filter(_._8.nonEmpty)
      val childItemIds   = comboItemRs.flatMap(_._8)

      db.run(itemDAO.items.filter(_.id inSet childItemIds).result)
        .map { items =>
          val itemMap = items.map(item => item._1 -> item).toMap
          comboItemRs.map { ci =>
            ci._1 -> {
              ci._8.map(id => itemMap.getOrElse(id, throw itemDAO.notFound(id)))
                   .map(item => item._1 -> item).toMap
            }
          }.toMap
        }
    }

    //------------/ Main method /----------------------
    cache.get[Map[Item.Id, Item]](ALL_ITEM_KEY).flatMap {
      case Some(itemM) =>
        Future.successful {
          ids.map(id => itemM.getOrElse(id, throw itemDAO.notFound(id)))
        }
      case None        =>
        for {
          targetItemRs <- db.run(itemDAO.items.filter(_.id inSet ids).result)
          childItemMap <- filterComboItems(targetItemRs)
        } yield {
          targetItemRs.map { targetItemR =>
            itemDAO.apply(targetItemR, childItemMap.getOrElse(targetItemR._1, Map.empty))
          }
        }
    }
  }

  override def store(items: Seq[Item]): Future[Unit] = db.run {
    itemDAO.items ++= items.map(itemDAO.unapply)
  }.map(_ => ())
}
