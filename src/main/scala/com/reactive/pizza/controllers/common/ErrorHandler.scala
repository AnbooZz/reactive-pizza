package com.reactive.pizza.controllers.common

import com.reactive.pizza.utils.{ EntityNotFoundException, ExistedUsernameException, PasswordNotMatchException, SecurityPolicyException, UnAuthorizedException }
import play.api.{ Configuration, Environment, Logging, OptionalSourceMapper }
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results.{ BadRequest, Forbidden, InternalServerError, Unauthorized }
import play.api.routing.Router

import javax.inject._
import scala.concurrent._

class ErrorHandler @Inject()(
  env:           Environment,
  conf:          Configuration,
  sourceMapper:  OptionalSourceMapper,
  router:        Provider[Router]
) extends DefaultHttpErrorHandler(env, conf, sourceMapper, router) with Logging {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    statusCode match {
      case _ => Future.successful(ErrorHandler.badRequest)
    }
  }

  override def onServerError(request: RequestHeader, ex: Throwable): Future[Result] = {
    ex.printStackTrace()
    logger.error(s"Happen error: ${ex.getMessage}")

    Future.successful(
      ex match {
        case e: PasswordNotMatchException => ErrorHandler.badRequest(e.getMessage)
        case e: ExistedUsernameException  => ErrorHandler.badRequest(e.getMessage)
        case _: UnAuthorizedException     => ErrorHandler.unAuthorized
        case _: IllegalArgumentException |
             _: EntityNotFoundException  |
             _: SecurityPolicyException   => ErrorHandler.badRequest
        case _                            => ErrorHandler.serverError
      })
  }
}

object ErrorHandler extends APIHelper {
  //--------------[ Error Response ]---------------------
  val serverError  = InternalServerError(writeFailure(UNKNOWN_REQUEST_MSG))
  val badRequest   = BadRequest(writeFailure(BAD_REQUEST_MSG))
  val unAuthorized = Unauthorized(writeFailure(UNAUTHORIZED_MSG))
  val noPermission = Forbidden(writeFailure(NOT_PERMITTED_MSG))

  //--------------[ Error Response ]---------------------
  def badRequest(msg: String) = BadRequest(writeFailure(msg))
}
