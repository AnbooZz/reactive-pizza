package com.reactive.pizza.jsons.writes

import com.reactive.pizza.models.cart.Cart
import play.api.libs.json.Json

case class CartDTO(
  id:          String,
  totalPrice:  String,
  coupon:      Option[CouponDTO],
  pickedItems: Seq[PickedItemDTO]
)

object CartDTO {
  //--------------[ Json converter ]------------------
  implicit val cartWriter = Json.writes[CartDTO]

  //-------------[ Methods ]----------------------
  def apply(cart: Cart): CartDTO =
    new CartDTO(
      id = cart.id.v,
      totalPrice = s"${cart.getTotalPrice}.000",
      coupon = cart.coupon.map(CouponDTO(_)),
      pickedItems = cart.pikItems.map(PickedItemDTO(_))
    )
}
