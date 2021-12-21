package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.UserRepository

import scala.concurrent.Future

class UserRepositoryImpl extends UserRepository {
  override def getByUsername(v: String): Future[Option[User]] = ???
}
