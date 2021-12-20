package com.reactive.pizza.models.user

case class User(
  id:       User.Id,
  email:    String,
  username: String,
  password: String
) {
  //------------[ Validations ]-----------------

}

object User {
  case class Id(v: String)
}
