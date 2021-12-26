package com.reactive.pizza.repositories.persistences.tables

import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.{ Item, PickedItem }
import com.reactive.pizza.models.order.Order
import com.reactive.pizza.models.order.Order.CustomerInfo
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import org.joda.time.DateTime
import play.api.libs.json.Json

import javax.inject.Inject

class OrderDAO @Inject()(dbComponent: MySqlDBComponent, itemDAO: ItemDAO, couponDAO: CouponDAO){
  import dbComponent.driver.api._

  type OrderTupled = (Order.Id, String, Option[Coupon.Id], Int, Int, Order.Status, DateTime, DateTime)

  private[OrderDAO] class OrderTable(tag: Tag) extends Table[OrderTupled](tag, "orders") with ColumnCustomType {
    //----------[ Columns ]----------------------
    def id: Rep[Order.Id]              = column[Order.Id]("id", O.PrimaryKey)
    def item: Rep[String]              = column[String]("item")
    def coupon: Rep[Option[Coupon.Id]] = column[Option[Coupon.Id]]("coupon_id")
    def customerId: Rep[Int]           = column[Int]("customer_id")
    def totalPrice: Rep[Int]           = column[Int]("total_price")
    def status: Rep[Order.Status]      = column[Order.Status]("status")
    def createdAt: Rep[DateTime]       = column[DateTime]("created_at")
    def updatedAt: Rep[DateTime]       = column[DateTime]("updated_at")

    def * = (id, item, coupon, customerId, totalPrice, status, createdAt, updatedAt)
  }

  private[OrderDAO] class CustomerInfoTable(tag: Tag) extends Table[Order.CustomerInfo](tag, "customer_infos") {
    //----------[ Columns ]----------------------
    def id: Rep[Int]              = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def fullname: Rep[String]     = column[String]("fullname")
    def phone: Rep[String]        = column[String]("phone")
    def address: Rep[String]      = column[String]("address")
    def memo: Rep[Option[String]] = column[Option[String]]("memo")

    def * = (id, fullname, phone, address, memo) <>[Order.CustomerInfo] (
      t => CustomerInfo(t._2, t._3, t._4, t._5),
      o => Some(0, o.fullName, o.phone, o.address, o.memo)
    )
  }

  val orderQuery    = TableQuery[OrderTable]
  val customerQuery = TableQuery[CustomerInfoTable]
  //-------------------[ Methods ]--------------------------
  def apply(c: OrderTupled, couponOpt: Option[Coupon], customer: CustomerInfo, itemMap: Map[Item.Id, Item]): Order = {
    val pikItemJson = Json.parse(c._2)
    val pikItems    = (pikItemJson \\ "itemId")
      .map(json => {
        val itemId = Item.Id(json.as[String])
        val item   = itemMap.getOrElse(itemId, throw itemDAO.notFound(itemId))

        PickedItem(pikItemJson, item)
      }).toSeq

    Order(c._1, pikItems, couponOpt, customer, c._5, c._6, c._7, c._8)
  }

  def unapply(c: Order, customerId: Int): OrderTupled = {
    (c.id, Json.toJson(c.pikItems).toString(), c.coupon.map(_.id), customerId, c.totalPrice, c.status, c.createdAt, c.updatedAt)
  }
}
