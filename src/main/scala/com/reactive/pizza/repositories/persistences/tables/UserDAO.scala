package com.reactive.pizza.repositories.persistences.tables

import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.persistences.{ ColumnCustomType, MySqlDBComponent }

import javax.inject.{ Inject, Singleton }

@Singleton
class UserDAO @Inject()(dbComponent: MySqlDBComponent) {
  import dbComponent.mysqlDriver.api._

  private[UserDAO] class UserTable(tag: Tag) extends Table[User](tag, "user") with ColumnCustomType {
    //----------[ Columns ]----------------------
    def id: Rep[User.Id]      = column[User.Id]("id", O.PrimaryKey)
    def username: Rep[String] = column[String]("username")
    def email: Rep[String]    = column[String]("email")
    def password: Rep[String] = column[String]("password")

    override def * = (id, username, email, password).<>[User] (
      t => User(t._1, t._2, t._3, t._4),
      User.unapply
    )
  }

  val users = TableQuery[UserTable]
}
