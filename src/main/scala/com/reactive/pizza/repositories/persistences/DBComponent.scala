package com.reactive.pizza.repositories.persistences

import slick.jdbc.{ JdbcProfile, MySQLProfile }

class MySqlDBComponent {
  val driver: JdbcProfile     = MySQLProfile
  val db: driver.api.Database = MySqlDB.connectionPool
}

object MySqlDB {
  import slick.jdbc.MySQLProfile.api._

  val connectionPool = Database.forConfig("slick.dbs.default")
}
