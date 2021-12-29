package com.reactive.pizza.controllers.common

import play.api.libs.json.{ Json, Writes }

import scala.concurrent.Future

trait APIHelper {
  //--------------[ Messages ]-----------------------
  protected val BAD_REQUEST_MSG     = "Request is wrong"
  protected val UNKNOWN_REQUEST_MSG = "Unknown error happened. Please contact to admin for helping"
  protected val UNAUTHORIZED_MSG    = "Authentication is failed"
  protected val NOT_PERMITTED_MSG   = "No permission to access this resource"
  
  //---------------[ Helper Methods ]--------------------------------
  def writeSuccess() = Json.obj("success" -> true)
  def writeSuccess[T](data: T)(implicit writer: Writes[T]) =
    Json.obj("success" -> true, "result" -> Json.toJson(data))
  def writeFailure(message: String) =
    Json.obj("success" -> false, "error" -> Json.obj("message" -> message))

  implicit def toFuture[T](data: T): Future[T] = Future.successful(data)
}
