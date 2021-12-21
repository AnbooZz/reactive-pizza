package com.reactive.pizza.repositories.persistences

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object DBConnection {
  val db = DatabaseConfig.forConfig[JdbcProfile]("reactive.pizza.db")

  println(
    """
      |HELLO WORLD
      |""".stripMargin)
}
