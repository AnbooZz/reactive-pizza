package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.cart.Cart
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.{ CartRepository, CouponRepository, ItemRepository }
import com.reactive.pizza.repositories.persistences.tables.CartDAO
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import play.api.libs.json.Json

import javax.inject.Inject
import scala.concurrent.{ ExecutionContext, Future }

class CartRepositoryImpl @Inject()(cartDAO: CartDAO, dbComponent: MySqlDBComponent)(
  itemRepository:   ItemRepository,
  couponRepository: CouponRepository
)(implicit val ec: ExecutionContext) extends CartRepository with ColumnCustomType {

  import dbComponent.mysqlDriver.api._
  private val db = dbComponent.dbAction

  override def findByUserId(id: User.Id): Future[Option[Cart]] = findBy {
    db.run(cartDAO.carts.filter(_.userId === id).result.headOption)
  }

  override def findById(id: Cart.Id): Future[Option[Cart]] = findBy {
    db.run(cartDAO.carts.filter(_.id === id).result.headOption)
  }

  override def update(cart: Cart): Future[Unit] = db.run {
    cartDAO.carts.filter(_.id === cart.id).update(cartDAO.unapply(cart))
  }.map(_ => ())

  override def store(cart: Cart): Future[Unit] = db.run {
    cartDAO.carts += cartDAO.unapply(cart)
  }.map(_ => ())

  private def findBy(cartRF: Future[Option[cartDAO.CartTupled]]): Future[Option[Cart]] = {
    for {
      cartR   <- cartRF
      itemMap <- cartR match {
        case Some(c) =>
          val itemIds = (Json.parse(c._2) \\ "itemId").map(_.as[String]).map(Item.Id).toSeq
          itemRepository.filterByIds(itemIds).map(_.map(item => item.id -> item).toMap)
        case None    =>
          Future.successful(Map.empty[Item.Id, Item])
      }
      coupon <- cartR match {
        case Some(c) if c._3.isDefined =>
          couponRepository.findById(c._3.get)
        case _                         =>
          Future.successful(None)
      }
    } yield {
      cartR.map(cartDAO.apply(_, coupon, itemMap))
    }
  }
}
