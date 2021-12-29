package com.reactive.pizza.repositories

import com.google.inject.ImplementedBy
import com.reactive.pizza.models.order.Order
import com.reactive.pizza.repositories.impls.OrderRepositoryImpl

import scala.concurrent.Future

@ImplementedBy(classOf[OrderRepositoryImpl])
trait OrderRepository {
  //-----------[ Queries ]-----------------
  //-----------[ Commands ]---------------
  def store(order: Order): Future[Unit]
}
