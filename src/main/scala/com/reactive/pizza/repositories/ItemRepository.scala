package com.reactive.pizza.repositories

import com.google.inject.ImplementedBy
import com.reactive.pizza.models.item.Item
import com.reactive.pizza.repositories.impls.ItemRepositoryImpl

import scala.concurrent.Future

@ImplementedBy(classOf[ItemRepositoryImpl])
trait ItemRepository {
  //-----------[ Queries ]-----------------
  def findAll: Future[Seq[Item]]
  def findById(id: Item.Id): Future[Option[Item]]
  def filterByIds(ids: Seq[Item.Id]): Future[Seq[Item]]
  //-----------[ Commands ]---------------
  def store(items: Seq[Item]): Future[Unit]
}
