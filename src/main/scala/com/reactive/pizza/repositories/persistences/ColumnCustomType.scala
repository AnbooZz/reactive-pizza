package com.reactive.pizza.repositories.persistences

import com.reactive.pizza.models.cart.Cart
import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.Description.SizeInfo
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.models.item.Item._
import com.reactive.pizza.models.order.Order
import com.reactive.pizza.models.user.User
import org.joda.time.DateTime
import play.api.libs.json.Json

import java.net.URL
import java.sql.Timestamp

trait ColumnCustomType {
  import slick.jdbc.MySQLProfile.api._

  //-----------[ User Custom ]-------------------
  implicit val userIdType = MappedColumnType.base[User.Id, String](id => id.v, v => User.Id(v))
  //-----------[ Item Custom ]-------------------
  implicit val itemIdType    = MappedColumnType.base[Item.Id, String](id => id.v,      v => Item.Id(v))
  implicit val urlType       = MappedColumnType.base[URL, String](url => url.toString, v => new URL(v))
  implicit val groupType     = MappedColumnType.base[Group, String](group => group.v,  v => Group(v))
  implicit val itemIdSeqType = MappedColumnType.base[Seq[Item.Id], String](
    itemIds => Json.toJson(itemIds.map(_.v)).toString,
    str     => if (str.isEmpty) Nil else Json.parse(str).as[Seq[String]].map(Item.Id)
  )
  implicit val ingredientSeqType = MappedColumnType.base[Seq[String], String](
    ing => Json.toJson(ing).toString,
    str => if (str.isEmpty) Nil else Json.parse(str).as[Seq[String]]
  )
  implicit val sizeInfoSeqType = MappedColumnType.base[Seq[SizeInfo], String](
    si  => Json.toJson(si).toString,
    str => if (str.isEmpty) Nil else Json.parse(str).as[Seq[SizeInfo]]
  )
  //-----------[ Coupon Custom ]-------------------
  implicit val couponIdType = MappedColumnType.base[Coupon.Id, String](id => id.v,     v => Coupon.Id(v))
  implicit val effectType   = MappedColumnType.base[Coupon.Effect, String](ef => ef.v, v => Coupon.Effect(v))
  implicit val unitType     = MappedColumnType.base[Coupon.Unit, String](u => u.v,     v => Coupon.Unit(v))
  //-----------[ Cart Custom ]-------------------
  implicit val cartIdType   = MappedColumnType.base[Cart.Id, String](id => id.v, v => Cart.Id(v))
  implicit val dateTimeType = MappedColumnType.base[DateTime, Timestamp](id => new Timestamp(id.getMillis), v => new DateTime(v))
  //-----------[ Order Custom ]-------------------
  implicit val orderIdType = MappedColumnType.base[Order.Id, String](id => id.v,   v => Order.Id(v))
  implicit val statusType  = MappedColumnType.base[Order.Status, String](s => s.v, v => Order.Status(v))
}
