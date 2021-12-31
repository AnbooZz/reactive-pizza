package com.reactive.pizza.controllers.common

import com.reactive.pizza.models.user.User
import play.api.libs.json.Writes
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents, Request, Result }

import scala.concurrent.Future

abstract class BaseController(cc: ControllerComponents) extends AbstractController(cc) with APIHelper with XssFilter {
  //----------[ Typed definitions ]-------------------
  type Req = Request[AnyContent]
  type Res = Future[Result]

  //---------------[ Helper Methods ]--------------------------------
  def success[T](data: T)(implicit writer: Writes[T]): Result = Ok(writeSuccess(data))
  def success(): Result = Ok(writeSuccess())

  def failed(status: Status)(message: String): Result = status(writeFailure(message))
  def notFound(message: String): Result    = failed(NotFound)(message)
  def badRequest(message: String): Result  = failed(BadRequest)(message)

  //---------------[ Wrapper Methods ]---------------------------------
  def withSecure(f: Req => Res) = Action.async { request =>
    f(checkBody(request))
  }
  def withAuth(checkAuth: (User => Req => Res, Req) => Res)(f: User => Req => Res) = Action.async { request =>
   checkAuth(f, request)
  }
}
