package com.reactive.pizza.controllers

import com.reactive.pizza.controllers.common.BaseController
import com.reactive.pizza.dummy.ItemDummy
import com.reactive.pizza.jsons.writes.ItemDTO
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.ItemRepository
import play.api.cache.Cached
import play.api.mvc.ControllerComponents

import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext

@Singleton
class ItemController @Inject()(
  cc:             ControllerComponents,
  itemRepository: ItemRepository,
  itemDummy:      ItemDummy
)(implicit val ec: ExecutionContext) extends BaseController(cc) {

  def list = Action.async {
    itemRepository.findAll.map {
      _.map(ItemDTO(_))
    }.map(success(_))
  }

  def get(id: String) = Action.async {
    itemRepository.findById(Item.Id(id)).map {
      case Some(v) => success(ItemDTO(v))
      case None    => badRequest(s"Not found item with id: $id")
    }
  }
}
