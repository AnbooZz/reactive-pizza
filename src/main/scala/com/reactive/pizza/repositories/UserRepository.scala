package com.reactive.pizza.repositories

import com.reactive.pizza.models.user.User
import scala.concurrent.Future

trait UserRepository {
  def getByUsername(v: String): Future[Option[User]]
}
