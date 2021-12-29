package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.UserRepository
import com.reactive.pizza.repositories.persistences.tables.UserDAO
import com.reactive.pizza.repositories.persistences.MySqlDBComponent

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class UserRepositoryImpl @Inject()(userDAO: UserDAO, dbComponent: MySqlDBComponent)(implicit val ec: ExecutionContext)
  extends UserRepository {

  import dbComponent.mysqlDriver.api._
  private val db = dbComponent.dbAction

  override def findByUsername(v: String): Future[Option[User]] = db.run {
    userDAO.users
      .filter(_.username === v)
      .result.headOption
  }

  override def store(user: User): Future[Unit] = db.run {
    userDAO.users += user
  }.map(_ => ())
}
