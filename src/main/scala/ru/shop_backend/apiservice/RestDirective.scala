package ru.shop_backend.apiservice

import ru.shop_backend.AppEnv
import ru.shop_backend.apiservice.ApiService.{AuthError, BadRequest, ErrorResponse}
import ru.shop_backend.db.services.UserDbService
import sttp.tapir.Endpoint
import sttp.tapir.model.UsernamePassword
import sttp.tapir.ztapir._
import zio.ZIO
import zio.logging.log

trait RestDirective {

  implicit class CustZEndpoint[I, O](e: Endpoint[I, ErrorResponse, O, Any]) {

    private def auth[R <: AppEnv](
        ) = {
      e.in(sttp.tapir.auth.basic[UsernamePassword]())
        .zServerLogicForCurrent { p: (I, UsernamePassword) =>
          for {
            userDb <- ZIO.service[UserDbService.Service]
            user <- userDb
              .find(p._2.username)
              .orElseFail(BadRequest("user not found"))
            _ <- ZIO.fail(AuthError("wrong password")).when(user.password != p._2.password.getOrElse(""))
          } yield (user, p._1)
        }
    }

    def logicWithAuth[R <: AppEnv](
        f: I => ZIO[R, Throwable, O]
    )(
        implicit err: Throwable => ErrorResponse
    ) = {
        auth().serverLogic {
        case ((_, i), _) =>
          for {
            _   <- log.info(s"Start request with $i  ")
            ret <- f(i).mapError(err)
          } yield ret

      }
    }

    def logicWithAdminAuth[R <: AppEnv](
        f: I => ZIO[R, Throwable, O]
    )(
        implicit err: Throwable => ErrorResponse
    ) = {
      auth().serverLogic {
        case ((user, i), _) =>
          for {
            _   <- log.info(s"Start request with $i  ")
            _   <- ZIO.fail(AuthError("not admin role")).when(user.role != "admin")
            ret <- f(i).mapError(err)
          } yield ret

      }
    }
  }

}
