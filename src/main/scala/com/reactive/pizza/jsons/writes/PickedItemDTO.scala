package com.reactive.pizza.jsons.writes

import com.reactive.pizza.models.item.PickedItem
import play.api.libs.json.Json

case class PickedItemDTO(
  id:       String,
  imgLink:  String,
  label:    String,
  price:    String,
  quantity: Int
)

object PickedItemDTO {
  //--------------[ Json converter ]------------------
  implicit val pItemWriter = Json.writes[PickedItemDTO]

  //-------------[ Methods ]----------------------
  def apply(pikItem: PickedItem): PickedItemDTO =
    new PickedItemDTO(
      id = pikItem.item.id.v,
      imgLink = pikItem.item.imgLink.toString,
      label = pikItem.getLabel,
      price = s"${pikItem.getPrice}.000",
      quantity = pikItem.quantity
    )
}
