package com.reactive.pizza.jsons.reads

import com.reactive.pizza.models.user.User
import play.api.libs.json.Json

case class LoginDTO(username: String, password: String)
case class RegisterDTO(email: String, username: String, password: String, rePassword: String) {
  def toUser = User.register(email, username, password, rePassword)
}

object AuthDTO {
  implicit val loginReader    = Json.reads[LoginDTO]
  implicit val registerReader = Json.reads[RegisterDTO]
}
