package com.reactive.pizza.jsons.reads

import com.reactive.pizza.models.order.Order.CustomerInfo
import play.api.libs.json.Json

case class OrderDTO(cartId: String, customerInfo: CustomerInfo)

object OrderDTO {
  implicit val customerInfoReader = Json.reads[CustomerInfo]
  implicit val orderReader        = Json.reads[OrderDTO]
}
