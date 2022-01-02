package com.reactive.pizza.repositories.persistences

import akka.actor.ActorSystem
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile

import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext

@Singleton
class MySqlDBComponent @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, akkaSystem: ActorSystem)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  val mysqlDriver = profile
  val dbAction    = db

  val dbEC: ExecutionContext = akkaSystem.dispatchers.lookup("expensive-db-lookups")
}
