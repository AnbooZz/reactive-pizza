package com.reactive.pizza.models.item

import java.net.URL
import com.reactive.pizza.models.item.Item.Group

abstract class Item {
  val id:      Item.Id
  val name:    String
  val descr:   Description
  val imgLink: URL
  val group:   Group

  //--------[ Validations]----------
}

case class SizableItem(
  id:      Item.Id,
  name:    String,
  descr:   SizeDescription,
  imgLink: URL,
  group:   Group
) extends Item {
  //--------[ Validations]----------
}

case class NoSizeableItem(
  id:      Item.Id,
  name:    String,
  descr:   Description,
  imgLink: URL,
  group:   Group,
  price:   Int
) extends Item {
  //--------[ Validations]----------
}

case class ComboItem(
  id:      Item.Id,
  name:    String,
  descr:   Description,
  imgLink: URL,
  price:   Int
) extends Item {
  override val group: Group = Item.Combo
  //--------[ Validations]----------
}

object Item {
  case class Id(v: String)
  //--------------//------------------
  sealed abstract class Group(val v: String)
  final case object Pizza  extends Group("Pizza")
  final case object BBQ    extends Group("BBQ")
  final case object Noodle extends Group("Noodle")
  final case object Salad  extends Group("Salad")
  final case object Drink  extends Group("Drink")
  final case object Combo  extends Group("Combo")
  final case object Other  extends Group("Other")
  object Group {
    def apply(v: String): Group = v match {
      case "Pizza"  => Pizza
      case "BBQ"    => BBQ
      case "Noodle" => Noodle
      case "Salad"  => Salad
      case "Drink"  => Drink
      case "Combo"  => Combo
      case "Other"  => Other

      case _        => throw new IllegalArgumentException(s"Illegal group: $v")
    }
  }
}
