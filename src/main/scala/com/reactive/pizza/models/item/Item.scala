package com.reactive.pizza.models.item

import java.net.URL

abstract class Item {
  val id: Item.Id
  val name: String
  val descr: Description
  val imgLink: URL


}

object Item {
  case class Id(v: String)

  sealed abstract class Group(val v: String)
  final case object Pizza  extends Group("Pizza")
  final case object BBQ    extends Group("BBQ")
  final case object Noodle extends Group("Noodle")
  final case object Salad  extends Group("Salad")
  final case object Drink  extends Group("Drink")
  final case object Combo  extends Group("Combo")
  final case object Other  extends Group("Other")


}
