package com.reactive.pizza.repositories

import com.google.inject.ImplementedBy
import com.reactive.pizza.models.cart.Cart
import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.impls.CartRepositoryImpl

import scala.concurrent.Future

@ImplementedBy(classOf[CartRepositoryImpl])
trait CartRepository {
  //-----------[ Queries ]-----------------
  def findByUserId(id: User.Id): Future[Option[Cart]]
  def findById(id: Cart.Id): Future[Option[Cart]]
  //-----------[ Commands ]---------------
  def update(cart: Cart): Future[Unit]
  def store(cart: Cart): Future[Unit]
}
