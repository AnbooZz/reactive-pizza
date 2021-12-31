package com.reactive.pizza.models.cart

import com.reactive.pizza.models.coupon.{ Coupon, MoneyCoupon }
import com.reactive.pizza.models.item.PickedItem
import com.reactive.pizza.models.user.User
import org.joda.time.DateTime

import scala.math.Ordered.orderingToOrdered

case class Cart(
  id:        Cart.Id,
  pikItems:  Seq[PickedItem],
  coupon:    Option[Coupon],
  userId:    User.Id,
  createdAt: DateTime,
  updatedAt: DateTime
) {
  //-----------------[ Validations ]--------------------
  require(createdAt <= updatedAt, "UpdatedAt must be after than CreatedAt")
  //---------------[ Methods ]----------------
  def getTotalPrice: Int = {
    val beforeUsingCoupon = pikItems.map(_.getPrice).sum

    coupon match {
      case Some(cp: MoneyCoupon) => cp.reduce(beforeUsingCoupon)
      case _                     => beforeUsingCoupon
    }

  }

  def addCoupon(coupon: Coupon): Cart = this.copy(coupon = Some(coupon))
}

object Cart {
  case class Id(v: String)
}
