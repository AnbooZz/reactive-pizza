package com.reactive.pizza.controllers

import com.reactive.pizza.controllers.common.BaseController
import com.reactive.pizza.jsons.reads.{ LoginDTO, RegisterDTO }
import com.reactive.pizza.repositories.UserRepository
import com.reactive.pizza.services.AuthService
import com.reactive.pizza.utils.ExistedUsernameException
import play.api.mvc.{ AnyContent, ControllerComponents, Request, Result }

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class AuthenController @Inject()(cc: ControllerComponents, userRepository: UserRepository, authService: AuthService)(implicit val ec: ExecutionContext)
  extends BaseController(cc) {

  //----------------[ Properties ]----------------
  private val LOGIN_ACTION    = "login"
  private val REGISTER_ACTION = "register"
  private val LOGIN_FAILED    = "Incorrect username or password"

  //----------------[ Methods ]-------------------
  def aggregate(action: String) = withSecure { request =>
    action match {
      case LOGIN_ACTION    => login(request)
      case REGISTER_ACTION => register(request)
      case _               => badRequest(BAD_REQUEST_MSG)
    }
  }

  private def login(request: Request[AnyContent]): Future[Result] = {
    import com.reactive.pizza.jsons.reads.AuthDTO.loginReader

    request.body.asJson.flatMap(_.asOpt[LoginDTO]) match {
      case Some(dto) =>
        for {
          userOpt <- userRepository.findByUsername(dto.username)
          isValid  = userOpt match {
            case Some(user) => user.isLogined(dto.username, dto.password)
            case None       => false
          }
        } yield {
          if (isValid) success().withSession(authService.SESSION_KEY -> userOpt.get.id.v)
          else         badRequest(LOGIN_FAILED)
        }
      case None      =>
        badRequest(BAD_REQUEST_MSG)
    }
  }

  private def register(request: Request[AnyContent]): Future[Result] = {
    import com.reactive.pizza.jsons.reads.AuthDTO.registerReader

    request.body.asJson.flatMap(_.asOpt[RegisterDTO]) match {
      case Some(dto) =>
        for {
          userOpt <- userRepository.findByUsername(dto.username)
          _ <- userOpt match {
            case Some(_) => throw new ExistedUsernameException(dto.username)
            case None    => userRepository.store(dto.toUser)
          }
        } yield { success() }

      case None      =>
        badRequest(BAD_REQUEST_MSG)
    }
  }
}
