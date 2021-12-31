package com.reactive.pizza.controllers

import com.reactive.pizza.controllers.common.BaseController
import com.reactive.pizza.jsons.reads.OrderDTO
import com.reactive.pizza.models.order.Order
import com.reactive.pizza.repositories.{ CartRepository, OrderRepository }
import com.reactive.pizza.services.AuthService
import com.reactive.pizza.utils.EntityNotFoundException
import play.api.mvc.ControllerComponents

import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext

@Singleton
class OrderController @Inject()(
  cc:              ControllerComponents,
  cartRepository:  CartRepository,
  orderRepository: OrderRepository,
  authService:     AuthService
)(implicit val ec: ExecutionContext) extends BaseController(cc) {

  def create(userId: String) = withAuth(authService.check) { user => request =>
    if (userId != user.id.v) throw new IllegalArgumentException(BAD_REQUEST_MSG)

    request.body.asJson.flatMap(_.asOpt[OrderDTO]) match {
      case Some(dto) =>
        for {
          cartOpt <- cartRepository.findByUserId(user.id)
          cart     = cartOpt.collect { case c if c.id.v == dto.cartId => c }
                            .getOrElse(throw new EntityNotFoundException(s"Not found cart with id: ${dto.cartId}"))
          order    = Order(user.id, cart, dto.customerInfo)
          _       <- orderRepository.store(order)
          _       <- cartRepository.update(cart.reset)
        } yield success()
      case None      =>
        badRequest(BAD_REQUEST_MSG)
    }
  }
}
