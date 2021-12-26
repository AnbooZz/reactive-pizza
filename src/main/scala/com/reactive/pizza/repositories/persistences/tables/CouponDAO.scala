package com.reactive.pizza.repositories.persistences.tables

import com.reactive.pizza.models.coupon.{ Coupon, GiftCoupon, MoneyCoupon }
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import com.reactive.pizza.utils.UnSupportedTypeException

import java.sql.Timestamp
import javax.inject.Inject

class CouponDAO @Inject()(dbComponent: MySqlDBComponent, itemDAO: ItemDAO) {
  import dbComponent.driver.api._

  type CouponTupled = (Coupon.Id, String, Timestamp, Coupon.Effect, String, Option[Coupon.Unit])

  private[CouponDAO] class CouponTable(tag: Tag) extends Table[CouponTupled](tag, "coupons") with ColumnCustomType {
    //----------[ Columns ]----------------------
    def id: Rep[Coupon.Id]             = column[Coupon.Id]("id", O.PrimaryKey)
    def description: Rep[String]       = column[String]("description")
    def expiredDate: Rep[Timestamp]    = column[Timestamp]("expired")
    def effect: Rep[Coupon.Effect]     = column[Coupon.Effect]("effect")
    def value: Rep[String]             = column[String]("value")
    def unit: Rep[Option[Coupon.Unit]] = column[Option[Coupon.Unit]]("unit")

    override def * = (id, description, expiredDate, effect, value, unit)
  }

  val couponQuery = TableQuery[CouponTable]
  //-------------[ Methods ]----------------------------
  def apply(c: CouponTupled, itemOpt: Option[itemDAO.ItemTupled]): Coupon = {
    c._6 match {
      case Some(u)  =>
        MoneyCoupon(c._1, c._2, c._3, c._4, c._5.toInt, u)
      case None     =>
        if (itemOpt.forall(_._1.v == c._5))
          GiftCoupon(c._1, c._2, c._3, c._4, itemDAO.apply(itemOpt.get, Map.empty))
        else
          throw itemDAO.notFound(Item.Id(c._5))
    }
  }

  def unapply(c: Coupon): Option[CouponTupled] = {
    c match {
      case gc: GiftCoupon  =>
        Some(gc.id, gc.descr, gc.expiredDate, gc.effect, gc.value.id.v, None)
      case mc: MoneyCoupon =>
        Some(mc.id, mc.descr, mc.expiredDate, mc.effect, s"${mc.value}", Some(mc.unit))
      case _               =>
        throw new UnSupportedTypeException("This type isn't belong to CouponType")
    }
  }
}
