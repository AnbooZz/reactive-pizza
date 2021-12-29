package com.reactive.pizza.repositories.persistences.tables

import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.{ Item, PickedItem }
import com.reactive.pizza.models.order.Order
import com.reactive.pizza.models.order.Order.CustomerInfo
import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import org.joda.time.DateTime
import play.api.libs.json.Json

import javax.inject.Inject

class OrderDAO @Inject()(dbComponent: MySqlDBComponent, itemDAO: ItemDAO){
  import dbComponent.mysqlDriver.api._

  type OrderTupled = (Order.Id, String, Option[Coupon.Id], Int, Order.Status, User.Id, DateTime, DateTime)

  private[OrderDAO] class OrderTable(tag: Tag) extends Table[OrderTupled](tag, "order") with ColumnCustomType {
    //----------[ Columns ]----------------------
    def id: Rep[Order.Id]              = column[Order.Id]("id", O.PrimaryKey)
    def item: Rep[String]              = column[String]("item")
    def coupon: Rep[Option[Coupon.Id]] = column[Option[Coupon.Id]]("coupon_id")
    def totalPrice: Rep[Int]           = column[Int]("total_price")
    def status: Rep[Order.Status]      = column[Order.Status]("status")
    def userId: Rep[User.Id]           = column[User.Id]("user_id")
    def createdAt: Rep[DateTime]       = column[DateTime]("created_at")
    def updatedAt: Rep[DateTime]       = column[DateTime]("updated_at")

    def * = (id, item, coupon, totalPrice, status, userId, createdAt, updatedAt)
  }

  type OrderCustomerTupled = (String, String, String, Option[String], Order.Id)

  private[OrderDAO] class OrderCustomerInfoTable(tag: Tag) extends Table[OrderCustomerTupled](tag, "order_customer_info") with ColumnCustomType {
    //----------[ Columns ]----------------------
    def fullname: Rep[String]     = column[String]("fullname")
    def phone: Rep[String]        = column[String]("phone")
    def address: Rep[String]      = column[String]("address")
    def memo: Rep[Option[String]] = column[Option[String]]("memo")
    def orderId: Rep[Order.Id]    = column[Order.Id]("order_id", O.PrimaryKey)

    def * = (fullname, phone, address, memo, orderId)
  }

  val orders    = TableQuery[OrderTable]
  val customers = TableQuery[OrderCustomerInfoTable]
  //-------------------[ Methods ]--------------------------
  def apply(c: OrderTupled, couponOpt: Option[Coupon], customer: CustomerInfo, itemMap: Map[Item.Id, Item]): Order = {
    val pikItemJson = Json.parse(c._2)
    val pikItems    = (pikItemJson \\ "itemId")
      .map(json => {
        val itemId = Item.Id(json.as[String])
        val item   = itemMap.getOrElse(itemId, throw itemDAO.notFound(itemId))

        PickedItem(pikItemJson, item)
      }).toSeq

    Order(c._1, pikItems, couponOpt, customer, c._4, c._5, c._6, c._7, c._8)
  }

  def unapply(c: Order): OrderTupled = {
    (c.id, Json.toJson(c.pikItems).toString(), c.coupon.map(_.id), c.totalPrice, c.status, c.userId, c.createdAt, c.updatedAt)
  }
  //-------------------[ Methods ]--------------------------
  def apply(c: OrderCustomerTupled) = Order.CustomerInfo(c._1, c._2, c._3, c._4)
  def unapply(c: Order.CustomerInfo, orderId: Order.Id): OrderCustomerTupled = (c.fullname, c.phone, c.address, c.memo, orderId)
}
