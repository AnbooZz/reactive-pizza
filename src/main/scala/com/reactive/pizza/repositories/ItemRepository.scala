package com.reactive.pizza.repositories

import com.reactive.pizza.models.item.Item

import scala.concurrent.Future

trait ItemRepository {
  //-----------[ Queries ]-----------------
  def findById(id: Item.Id): Future[Option[Item]]
  def filterByIds(ids: Seq[Item.Id]): Future[Seq[Item]]
  //-----------[ Commands ]---------------
  def store(items: Seq[Item]): Future[Unit]
}
