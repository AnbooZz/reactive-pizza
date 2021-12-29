package com.reactive.pizza.controllers.common

import akka.stream.Materializer
import play.api.mvc.{ Filter, RequestHeader, Result }

import javax.inject.Inject
import scala.concurrent.{ ExecutionContext, Future }

class HttpFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter with XssFilter {

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    for {
      result <- nextFilter(checkRequestParam(requestHeader))
    } yield {
      result
    }
  }
}
