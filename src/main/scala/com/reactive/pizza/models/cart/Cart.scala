package com.reactive.pizza.models.cart

import com.reactive.pizza.models.coupon.Coupon
import com.reactive.pizza.models.item.PickedItem
import com.reactive.pizza.models.user.User
import org.joda.time.DateTime

import scala.math.Ordered.orderingToOrdered

class Cart(
  id:        Cart.Id,
  pikItems:  Seq[PickedItem],
  coupon:    Option[Coupon],
  userId:    User.Id,
  createdAt: DateTime,
  updatedAt: DateTime
) {
  //-----------------[ Validations ]--------------------
  require(createdAt <= updatedAt, "UpdatedAt must be after than CreatedAt")
}

object Cart {
  case class Id(v: String)
}
