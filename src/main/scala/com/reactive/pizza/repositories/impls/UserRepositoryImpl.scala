package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.UserRepository
import com.reactive.pizza.repositories.persistences.tables.UserDAO

import javax.inject.Inject
import scala.concurrent.Future

class UserRepositoryImpl @Inject()(userDAO: UserDAO) extends UserRepository {
  override def getByUsername(v: String): Future[Option[User]] = ???
}
