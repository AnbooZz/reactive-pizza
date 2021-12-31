package com.reactive.pizza.repositories

import com.google.inject.ImplementedBy
import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.impls.UserRepositoryImpl

import scala.concurrent.Future

@ImplementedBy(classOf[UserRepositoryImpl])
trait UserRepository {
  //-----------[ Queries ]-----------------
  def findById(userId: User.Id): Future[Option[User]]
  def findByUsername(v: String): Future[Option[User]]
  //-----------[ Commands ]---------------
  def store(user: User): Future[Unit]
}
