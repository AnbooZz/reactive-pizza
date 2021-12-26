package com.reactive.pizza.repositories.persistences.tables

import com.reactive.pizza.models.item.{ Item, _ }
import com.reactive.pizza.models.item.Description.SizeInfo
import com.reactive.pizza.models.item.Item.Group
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }
import com.reactive.pizza.utils.{ DBMappingException, EntityNotFoundException, UnSupportedTypeException }

import java.net.URL
import javax.inject.Inject

class ItemDAO @Inject()(dbComponent: MySqlDBComponent) {
  import dbComponent.driver.api._

  type ItemTupled = (Item.Id, String, Seq[String], Seq[SizeInfo], URL, Group, Boolean, Boolean, Seq[Item.Id], Option[Int])

  private[ItemDAO] class ItemTable(tag: Tag)  extends Table[ItemTupled](tag, "item") with ColumnCustomType  {
    //----------[ Columns ]----------------------
    def id: Rep[Item.Id]               = column[Item.Id]("id", O.PrimaryKey)
    def name: Rep[String]              = column[String]("name")
    def ingredients: Rep[Seq[String]]  = column[Seq[String]]("ingredient")
    def sizeInfos: Rep[Seq[SizeInfo]]  = column[Seq[SizeInfo]]("size_info")
    def imgLink: Rep[URL]              = column[URL]("img_link")
    def group: Rep[Group]              = column[Group]("group")
    def isSizable: Rep[Boolean]        = column[Boolean]("is_sizable")
    def isCombo: Rep[Boolean]          = column[Boolean]("is_combo")
    def itemIds: Rep[Seq[Item.Id]]     = column[Seq[Item.Id]]("item_id_seq")
    def price: Rep[Option[Int]]        = column[Option[Int]]("price")

    override def * = (id, name, ingredients, sizeInfos, imgLink, group, isSizable, isCombo, itemIds, price)
  }

  val itemQuery = TableQuery[ItemTable]
  //----------[ Methods ]----------------------
  def apply(thisR: ItemTupled, thatRMap: Map[Item.Id, ItemTupled]): Item = {
    thisR._7 -> thisR._8 match {
      case (true, false) =>
        val description = SizeDescription(thisR._3, thisR._4)
        SizableItem(thisR._1, thisR._2, description, thisR._5, thisR._6)

      case (false, true) =>
        val description = new Description(thisR._3)
        val items       = thisR._9.map(id => thatRMap.get(id)    // Option[ItemTupled]
                                  .map(m => apply(m, Map.empty)) // Option[Item]
                                  .getOrElse(throw notFound(id)))
        ComboItem(thisR._1, thisR._2, description, thisR._5, items, thisR._10.getOrElse(0))

      case (false, false) =>
        val description = new Description(thisR._3)
        NoSizeableItem(thisR._1, thisR._2, description, thisR._5, thisR._6, thisR._10.getOrElse(0))

      case _              =>
        throw new DBMappingException("Item mapping error by conflict isSizable and isCombo")
    }
  }

  def unapply(item: Item): Option[ItemTupled] = item match {
    case s: SizableItem     =>
      Some(s.id, s.name, s.descr.ingredients, s.descr.sizeInfo, s.imgLink, s.group, true, false, Nil, None)
    case c: ComboItem       =>
      Some(c.id, c.name, c.descr.ingredients, Nil, c.imgLink, c.group, false, true, c.items.map(_.id), Some(c.price))
    case ns: NoSizeableItem =>
      Some(ns.id, ns.name, ns.descr.ingredients, Nil, ns.imgLink, ns.group, false, false, Nil, Some(ns.price))
    case _                  =>
      throw new UnSupportedTypeException("This type isn't belong to ItemType")
  }
  //----------------[ Exceptions ]-----------------------------
  private[tables] def notFound(id: Item.Id): EntityNotFoundException = new EntityNotFoundException(s"Not found entity Item with id: ${id.v}")
}
