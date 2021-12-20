package com.reactive.pizza.models.item

import com.reactive.pizza.models.item.Description.Size

case class PickedItem(item: Item, quantity: Int, size: Option[Size]) {
  //---------------[ Validations]------------------
}
