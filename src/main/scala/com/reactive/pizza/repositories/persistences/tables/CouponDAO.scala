package com.reactive.pizza.repositories.persistences.tables

import com.reactive.pizza.models.coupon.{ Coupon, GiftCoupon, MoneyCoupon }
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import com.reactive.pizza.utils.UnSupportedTypeException
import play.api.libs.json.Json

import java.sql.Timestamp
import javax.inject.{ Inject, Singleton }

@Singleton
class CouponDAO @Inject()(dbComponent: MySqlDBComponent, itemDAO: ItemDAO) {
  import dbComponent.mysqlDriver.api._

  type CouponTupled = (Coupon.Id, String, Timestamp, Seq[Coupon.Effect], String, Option[Coupon.Unit])

  private[CouponDAO] class CouponTable(tag: Tag) extends Table[CouponTupled](tag, "coupon") with ColumnCustomType {
    //----------[ Columns ]----------------------
    def id: Rep[Coupon.Id]              = column[Coupon.Id]("id", O.PrimaryKey)
    def description: Rep[String]        = column[String]("description")
    def expiredDate: Rep[Timestamp]     = column[Timestamp]("expired_date")
    def effect: Rep[Seq[Coupon.Effect]] = column[Seq[Coupon.Effect]]("effect_place")
    def value: Rep[String]              = column[String]("value")
    def unit: Rep[Option[Coupon.Unit]]  = column[Option[Coupon.Unit]]("unit")

    override def * = (id, description, expiredDate, effect, value, unit)
  }

  val coupons = TableQuery[CouponTable]
  //-------------[ Methods ]----------------------------
  def apply(c: CouponTupled, items: Seq[Item]): Coupon = {
    c._6 match {
      case Some(u)  =>
        MoneyCoupon(c._1, c._2, c._3, c._4, c._5.toInt, u)
      case None     =>
        val itemMap    = items.map(it => it.id -> it).toMap
        val validItems = Json.parse(c._5).as[Seq[String]]
          .map(Item.Id).map(id => itemMap.getOrElse(id, throw itemDAO.notFound(id)))

        GiftCoupon(c._1, c._2, c._3, c._4, validItems)
    }
  }

  def unapply(c: Coupon): CouponTupled = {
    c match {
      case gc: GiftCoupon  =>
        (gc.id, gc.descr, gc.expiredDate, gc.effects, Json.toJson(gc.values.map(_.id.v)).toString(), None)
      case mc: MoneyCoupon =>
        (mc.id, mc.descr, mc.expiredDate, mc.effects, s"${mc.value}", Some(mc.unit))
      case _               =>
        throw new UnSupportedTypeException("This type isn't belong to CouponType")
    }
  }
}
