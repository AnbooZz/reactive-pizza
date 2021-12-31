package com.reactive.pizza.controllers

import com.reactive.pizza.controllers.common.BaseController
import com.reactive.pizza.jsons.writes.CartDTO
import com.reactive.pizza.repositories.CartRepository
import com.reactive.pizza.services.AuthService
import play.api.mvc.ControllerComponents

import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext

@Singleton
class CartController @Inject()(cc: ControllerComponents, cartRepository: CartRepository, authService: AuthService)
  (implicit val ec: ExecutionContext) extends BaseController(cc){

  def getByUser(userId: String) = withAuth(authService.check) { user => _ =>
    if (userId != user.id.v) {
      badRequest(BAD_REQUEST_MSG)
    } else {
      cartRepository.findByUserId(user.id)
        .map(_.map(CartDTO(_)))
        .map(success(_))
    }
  }
}
