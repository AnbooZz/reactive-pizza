package com.reactive.pizza.repositories.persistences

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile

import javax.inject.{ Inject, Singleton }

@Singleton
class MySqlDBComponent @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  val mysqlDriver = profile
  val dbAction    = db
}
