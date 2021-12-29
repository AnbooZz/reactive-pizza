package com.reactive.pizza.repositories.persistences.tables

import com.reactive.pizza.models.coupon.{ Coupon, GiftCoupon, MoneyCoupon }
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import com.reactive.pizza.utils.UnSupportedTypeException

import java.sql.Timestamp
import javax.inject.{ Inject, Singleton }

@Singleton
class CouponDAO @Inject()(dbComponent: MySqlDBComponent, itemDAO: ItemDAO) {
  import dbComponent.mysqlDriver.api._

  type CouponTupled = (Coupon.Id, String, Timestamp, Coupon.Effect, String, Option[Coupon.Unit])

  private[CouponDAO] class CouponTable(tag: Tag) extends Table[CouponTupled](tag, "coupon") with ColumnCustomType {
    //----------[ Columns ]----------------------
    def id: Rep[Coupon.Id]             = column[Coupon.Id]("id", O.PrimaryKey)
    def description: Rep[String]       = column[String]("description")
    def expiredDate: Rep[Timestamp]    = column[Timestamp]("expired_date")
    def effect: Rep[Coupon.Effect]     = column[Coupon.Effect]("effect_place")
    def value: Rep[String]             = column[String]("value")
    def unit: Rep[Option[Coupon.Unit]] = column[Option[Coupon.Unit]]("unit")

    override def * = (id, description, expiredDate, effect, value, unit)
  }

  val coupons = TableQuery[CouponTable]
  //-------------[ Methods ]----------------------------
  def apply(c: CouponTupled, itemOpt: Option[Item]): Coupon = {
    c._6 match {
      case Some(u)  =>
        MoneyCoupon(c._1, c._2, c._3, c._4, c._5.toInt, u)
      case None     =>
        if (itemOpt.forall(_.id.v == c._5))
          GiftCoupon(c._1, c._2, c._3, c._4, itemOpt.get)
        else
          throw itemDAO.notFound(Item.Id(c._5))
    }
  }

  def unapply(c: Coupon): CouponTupled = {
    c match {
      case gc: GiftCoupon  =>
        (gc.id, gc.descr, gc.expiredDate, gc.effect, gc.value.id.v, None)
      case mc: MoneyCoupon =>
        (mc.id, mc.descr, mc.expiredDate, mc.effect, s"${mc.value}", Some(mc.unit))
      case _               =>
        throw new UnSupportedTypeException("This type isn't belong to CouponType")
    }
  }
}
