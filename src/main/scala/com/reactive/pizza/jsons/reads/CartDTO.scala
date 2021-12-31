package com.reactive.pizza.jsons.reads

import com.reactive.pizza.models.item.Item
import play.api.libs.json.Json

case class CouponDTO(code: String)
case class CartDTO(items: Seq[PickedItemDTO], coupon: Option[CouponDTO]) {
  def getItemIds = items.map(it => Item.Id(it.itemId))
}

object CartDTO {
  implicit val couponReader = Json.reads[CouponDTO]
  implicit val cartReader   = Json.reads[CartDTO]
}
