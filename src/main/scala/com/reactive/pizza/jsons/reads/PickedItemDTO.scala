package com.reactive.pizza.jsons.reads

import com.reactive.pizza.models.item.{ Item, PickedItem }
import com.reactive.pizza.models.item.Description.Size
import play.api.libs.json.Json

case class PickedItemDTO(itemId: String, quantity: Int, size: Option[String], operation: Option[String]) {
  def toPikItem(item: Item) = PickedItem(item, quantity, size.map(Size(_)))
  def isAdd: Boolean        = operation.contains(PickedItemDTO.ADD_OPERATION)
  def isReduce: Boolean     = operation.contains(PickedItemDTO.REDUCE_OPERATION)
}

object PickedItemDTO {
  //-----------[ Json converter ]------------------
  implicit val pItemReader = Json.reads[PickedItemDTO]
  //-----------[ Properties ]------------------
  val ADD_OPERATION    = "Add"
  val REDUCE_OPERATION = "Reduce"
}
