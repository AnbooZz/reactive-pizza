package com.reactive.pizza.repositories.impls

import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.UserRepository
import com.reactive.pizza.repositories.persistences.tables.UserDAO
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }

import javax.inject._
import scala.concurrent.Future

@Singleton
class UserRepositoryImpl @Inject()(userDAO: UserDAO, dbComponent: MySqlDBComponent)
  extends UserRepository with ColumnCustomType {

  //--------------[ Properties ]---------------------------
  import dbComponent.mysqlDriver.api._
  private val db          = dbComponent.dbAction
  private implicit val ec = dbComponent.dbEC

  //----------[ Methods ]---------------------
  override def findByUsername(v: String): Future[Option[User]] = db.run {
    userDAO.users
      .filter(_.username === v)
      .result.headOption
  }

  override def findById(userId: User.Id): Future[Option[User]] = db.run {
    userDAO.users
      .filter(_.id === userId)
      .result.headOption
  }

  override def store(user: User): Future[Unit] = db.run {
    userDAO.users += user
  }.map(_ => ())
}
