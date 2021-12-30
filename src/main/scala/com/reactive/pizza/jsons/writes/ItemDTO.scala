package com.reactive.pizza.jsons.writes

import com.reactive.pizza.models.item.{ ComboItem, Item, NoSizeableItem, SizableItem }
import play.api.libs.json.Json

case class ItemDTO(
  id:          String,
  name:        String,
  description: ItemDTO.DescriptionDTO,
  imgLink:     String,
  group:       String,
  price:       Option[String]
)

object ItemDTO {
  //--------------[ Class definitions ]----------------------
  case class SizeInfoDTO(size: String, cm: Int, price: String)
  case class DescriptionDTO(
    ingredients: Seq[String],
    extraText:   Option[String],
    sizeInfos:   Seq[SizeInfoDTO]
  )

  //--------------[ Json converters ]-------------------------
  implicit val sizeInfoWriter    = Json.writes[SizeInfoDTO]
  implicit val descriptionWriter = Json.writes[DescriptionDTO]
  implicit val itemWriter        = Json.writes[ItemDTO]

  //--------------[ Methods ]-------------------------
  def apply(item: Item): ItemDTO = {
    item match {
      case si: SizableItem    =>
        val sizeInfoDTOs   = si.descr.sizeInfo.map { sf =>
          SizeInfoDTO(sf.size.v, sf.cm, s"${sf.price}.000")
        }
        val descriptionDTO = DescriptionDTO(si.descr.ingredients, si.descr.extraText, sizeInfoDTOs)

        new ItemDTO(
          id          = si.id.v,
          name        = si.name,
          description = descriptionDTO,
          imgLink     = si.imgLink.toString,
          group       = si.group.v,
          price       = None
        )
      case ni: NoSizeableItem =>
        val descriptionDTO = DescriptionDTO(ni.descr.ingredients, ni.descr.extraText, Nil)
        new ItemDTO(
          id          = ni.id.v,
          name        = ni.name,
          description = descriptionDTO,
          imgLink     = ni.imgLink.toString,
          group       = ni.group.v,
          price       = Some(s"${ni.price}.000")
        )
      case ci: ComboItem      =>
        val descriptionDTO = DescriptionDTO(ci.descr.ingredients, ci.descr.extraText, Nil)
        new ItemDTO(
          id          = ci.id.v,
          name        = ci.name,
          description = descriptionDTO,
          imgLink     = ci.imgLink.toString,
          group       = ci.group.v,
          price       = Some(s"${ci.price}.000")
        )
    }
  }




}
