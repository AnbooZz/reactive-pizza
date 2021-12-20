package com.reactive.pizza.models.user

case class User(
  id:       User.Id,
  email:    String,
  username: String,
  password: String
) {
  //------------[ Validations ]-----------------
  require(1 <= email.length && email.length <= 255,       "Email must be from 1 to 255 characters")
  require(1 <= username.length && username.length <= 255, "Username must be from 1 to 255 characters")
  require(1 <= password.length && password.length <= 255, "Password must be from 1 to 255 characters")
}

object User {
  case class Id(v: String)
}
