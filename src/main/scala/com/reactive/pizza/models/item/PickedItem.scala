package com.reactive.pizza.models.item

import com.reactive.pizza.models.item.Description.Size
import play.api.data.format
import play.api.libs.json.{ Json, JsValue, Writes }

import scala.annotation.tailrec

case class PickedItem(item: Item, quantity: Int, size: Option[Size]) {
  //---------------[ Validations ]------------------
  require(quantity > 0, "Item quantity must be greater than 0")
}

object PickedItem {
  implicit val pikItemWriter: Writes[PickedItem] = (pi: PickedItem) => {
    Json.obj(
      "itemId" -> pi.item.id.v,
      "quantity" -> pi.quantity,
      "size"     -> pi.size.map(_.v)
    )
  }

  def apply(json: JsValue, item: Item): PickedItem = {
    val quantity = (json \ "quantity").as[Int]
    val size     = (json \ "size").asOpt[String].map(Size.apply)

    PickedItem(item, quantity, size)
  }
}
