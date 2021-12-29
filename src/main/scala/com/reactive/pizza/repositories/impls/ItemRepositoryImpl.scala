package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.ItemRepository
import com.reactive.pizza.repositories.persistences.tables.ItemDAO
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import com.reactive.pizza.utils.RollbackException

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ItemRepositoryImpl @Inject()(itemDAO: ItemDAO, dbComponent: MySqlDBComponent)(implicit val ec: ExecutionContext)
  extends ItemRepository with ColumnCustomType {

  import dbComponent.mysqlDriver.api._
  private val db = dbComponent.dbAction

  override def findById(id: Item.Id): Future[Option[Item]] = {
    for {
      targetItemR <- db.run(itemDAO.items.filter(_.id === id).result.headOption)
      childItemRs <- targetItemR match {
        case Some(v) if v._8.nonEmpty =>
          db.run(itemDAO.items.filter(_.id inSet v._8).result)
        case _                        =>
          Future.successful(Nil)
      }
    } yield {
      val childItemRMap = childItemRs.map(r => r._1 -> itemDAO.apply(r, Map.empty)).toMap
      targetItemR.map(itemDAO.apply(_, childItemRMap))
    }
  }

  override def filterByIds(ids: Seq[Item.Id]): Future[Seq[Item]] = {
    // Map[Item.Id, Map[Item.Id, Item]] -> Map[ComboItemId, Map[ChildItemId, ChildItem]]
    def filterComboItems(targetItemRs: Seq[itemDAO.ItemTupled]): Future[Map[Item.Id, Map[Item.Id, Item]]] = {
      val comboItemRs    = targetItemRs.filter(_._8.nonEmpty)
      val childItemIds   = comboItemRs.flatMap(_._8)

      db.run(itemDAO.items.filter(_.id inSet childItemIds).result)
        .map(_.map(itemDAO.apply(_, Map.empty)))
        .map { items =>
          val itemMap = items.map(item => item.id -> item).toMap
          comboItemRs.map { ci =>
            ci._1 -> {
              ci._8.map(id => itemMap.getOrElse(id, throw itemDAO.notFound(id)))
                   .map(item => item.id -> item).toMap
            }
          }.toMap
        }
    }

    for {
      targetItemRs <- db.run(itemDAO.items.filter(_.id inSet ids).result)
      childItemMap <- filterComboItems(targetItemRs)
    } yield {
      targetItemRs.map { targetItemR =>
        itemDAO.apply(targetItemR, childItemMap.getOrElse(targetItemR._1, Map.empty))
      }
    }
  }

  override def store(items: Seq[Item]): Future[Unit] = db.run {
    (itemDAO.items ++= items.map(itemDAO.unapply))
      .flatMap { _ =>
        DBIO.failed(new RollbackException("Rollback for store item list"))
      }.transactionally
  }
}
