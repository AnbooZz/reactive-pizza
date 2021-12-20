package com.reactive.pizza.models.item

import com.reactive.pizza.models.item.Description.Size

case class PickedItem(item: Item, quantity: Int, size: Option[Size]) {
  //---------------[ Validations ]------------------
  require(quantity > 0, "Item quantity must be greater than 0")
}
