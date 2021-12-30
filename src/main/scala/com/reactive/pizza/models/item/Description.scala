package com.reactive.pizza.models.item

import com.reactive.pizza.models.item.Description.SizeInfo
import com.reactive.pizza.utils.Message
import play.api.libs.json.{ JsObject, Json, JsResult, JsSuccess, JsValue, OFormat }

class Description(val ingredients: Seq[String]) {
  val extraText: Option[String] = Some(Message.EXTRA_TEXT)

  //----------[ Validations ]------------
  require(
    ingredients.forall(v => 1 <= v.length && v.length <= 255),
    "Contain invalid ingredient"
  )
}

case class SizeDescription(override val ingredients: Seq[String], sizeInfo: Seq[SizeInfo]) extends Description(ingredients)

object Description {
  //-------------[ Types ]---------------------
  sealed abstract class Size(val v: String)
  object Size {
    final case object S extends Size("S")
    final case object M extends Size("M")
    final case object L extends Size("L")

    def apply(v: String): Size = v match {
      case "S" => S
      case "M" => M
      case "L" => L

      case _   => throw new IllegalArgumentException(s"Illegal size value: $v")
    }
  }
  //-----------------//-------------------
  case class SizeInfo(size: Size, cm: Int, price: Int) {
    //------------[ Validations ]-------------
    require(0 < cm,    "Cm must be greater than 0")
    require(0 < price, "Price must be greater than 0")
  }
  //-------------[ Json converters ]----------------
  implicit val sizeInfoFormatter = new OFormat[SizeInfo] {
    override def reads(json: JsValue): JsResult[SizeInfo] = JsSuccess(
      SizeInfo(
        Size((json \ "size").as[String]),
        (json \ "cm").as[Int],
        (json \ "price").as[Int]
      )
    )

    override def writes(o: SizeInfo): JsObject = Json.obj(
      "size"  -> o.size.v,
      "cm"    -> o.cm,
      "price" -> o.price
    )
  }
}
