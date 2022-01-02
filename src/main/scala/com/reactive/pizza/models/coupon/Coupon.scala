package com.reactive.pizza.models.coupon

import com.reactive.pizza.models.coupon.Coupon._
import com.reactive.pizza.models.item.Item
import org.joda.time.DateTime

abstract class Coupon {
  val id:          Coupon.Id
  val descr:       String
  val expiredDate: DateTime
  val effects:     Seq[Effect]

  //----------[ Validations ]--------------
  require(1 <= descr.length && descr.length <= 255, "Description of coupon must be from 1 to 255 characters")

  //----------[ Methods ]------------------
  def isExpired: Boolean = {
    val now = new DateTime()
    expiredDate.isBefore(now)
  }
}

case class GiftCoupon(
  id:          Coupon.Id,
  descr:       String,
  expiredDate: DateTime,
  effects:     Seq[Effect],
  values:      Seq[Item]
) extends Coupon

case class MoneyCoupon(
  id:          Coupon.Id,
  descr:       String,
  expiredDate: DateTime,
  effects:     Seq[Effect],
  value:       Int,
  unit:        Unit
) extends Coupon {
  //----------[ Validations ]--------------
  require(0 < value, "Value of coupon must be greater than 0")
  //----------[ Methods ]------------------
  def reduce(price: Int): Int = {
    unit match {
      case Unit.Number  => price - value
      case Unit.Percent => price - price * value / 100
    }
  }
}

object Coupon {
  //-----------[ Class definitions ]-----------------------
  case class Id(v: String)
  //------------//--------------
  sealed abstract class Effect(val v: String)
  object Effect {
    final case object Online extends Effect("Online")
    final case object Store  extends Effect("Store")

    def apply(v: String): Effect = v match {
      case "Online" => Online
      case "Store"  => Store

      case _        => throw new IllegalArgumentException(s"Illegal effect value: $v")
    }
  }
  //------------//--------------
  sealed abstract class Unit(val v: String)
  object Unit {
    final case object Percent extends Unit("Percent")
    final case object Number  extends Unit("Number")

    def apply(v: String): Unit = v match {
      case "Percent" => Percent
      case "Number"  => Number

      case _         => throw new IllegalArgumentException(s"Illegal unit of coupon: $v")
    }
  }
}
