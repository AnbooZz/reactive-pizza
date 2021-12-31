package com.reactive.pizza.services

import com.reactive.pizza.models.user.User
import com.reactive.pizza.repositories.UserRepository
import com.reactive.pizza.utils.UnAuthorizedException
import play.api.cache.AsyncCacheApi
import play.api.mvc.{ Request, Result }

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.duration.DurationInt

@Singleton
class AuthService @Inject()(userRepository: UserRepository, cached: AsyncCacheApi)(implicit val ec: ExecutionContext) {
  //--------------[ Properties ]---------------------
  private val CACHE_TIMEOUT = 1.hours

  val SESSION_KEY = "id"

  def check[T](f: User => Request[T] => Future[Result], req: Request[T]): Future[Result] = {
    val userId = req.session.get(SESSION_KEY).getOrElse(throw new UnAuthorizedException)
    cached.get[User](s"user.$userId").flatMap {
      case Some(user) =>
        f(user)(req)

      case None       =>
        userRepository.findById(User.Id(userId))
          .map(_.getOrElse(throw new UnAuthorizedException))
          .flatMap { user =>
            f(user)(req) andThen { _ =>
              cached.set(SESSION_KEY, user, CACHE_TIMEOUT)
            }
          }
    }
  }
}
