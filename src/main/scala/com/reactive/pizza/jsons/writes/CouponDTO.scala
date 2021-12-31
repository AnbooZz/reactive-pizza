package com.reactive.pizza.jsons.writes

import com.reactive.pizza.models.coupon.Coupon
import play.api.libs.json.Json

case class CouponDTO(code: String)

object CouponDTO {
  //--------------[ Json converter ]------------------
  implicit val couponWriter = Json.writes[CouponDTO]

  //-------------[ Methods ]----------------------
  def apply(coupon: Coupon): CouponDTO = new CouponDTO(coupon.id.v)
}
