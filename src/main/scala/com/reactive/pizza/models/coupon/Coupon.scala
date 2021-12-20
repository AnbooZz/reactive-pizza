package com.reactive.pizza.models.coupon

import com.reactive.pizza.models.coupon.Coupon.Effect
import com.reactive.pizza.models.item.Item
import org.joda.time.DateTime

abstract class Coupon {
  val id:          Coupon.Id
  val descr:       String
  val expiredDate: DateTime
  val effect:      Effect

  //----------[ Validations ]--------------
}

case class GiftCoupon(
  id:          Coupon.Id,
  descr:       String,
  expiredDate: DateTime,
  effect:      Effect,
  value:       Item
) extends Coupon

case class MoneyCoupon(
  id:          Coupon.Id,
  descr:       String,
  expiredDate: DateTime,
  effect:      Effect,
  value:       Int,
  unit:        Unit
) extends Coupon {
  //----------[ Validations ]--------------
}

object Coupon {
  case class Id(v: String)
  //------------//--------------
  sealed abstract class Effect(val v: String)
  final case object Online extends Effect("Online")
  final case object Store  extends Effect("Store")
  object Effect {
    def apply(v: String): Effect = v match {
      case "Online" => Online
      case "Store"  => Store

      case _        => throw new IllegalArgumentException(s"Illegal effect value: $v")
    }
  }
  //------------//--------------
  sealed abstract class Unit(val v: String)
  final case object Percent extends Unit("Percent")
  final case object Number  extends Unit("Number")
  object Unit {
    def apply(v: String): Unit = v match {
      case "Percent" => Percent
      case "Number"  => Number

      case _         => throw new IllegalArgumentException(s"Illegal unit of coupon: $v")
    }
  }
}
