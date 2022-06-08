package ru.shop_backend.apiservice

import io.circe.generic.auto._
import ru.shop_backend.AppEnv
import ru.shop_backend.apiservice.ApiService.{BadRequest, ErrorResponse}
import ru.shop_backend.apiservice.services.user.Models
import ru.shop_backend.db.services.UserDbService
import sttp.model.StatusCode
import sttp.tapir.Endpoint
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.model.UsernamePassword
import sttp.tapir.ztapir._
import zhttp.http.{Http, Request, Response}
import zio.{Has, ZIO}
import zio.logging.Logging

case class ServiceInfo(tagName: String)

trait RestServiceCore {
  def info: ServiceInfo

  def endpoints: Iterable[Endpoint[_, _, _, _]]

  def routes: Http[AppEnv, Throwable, Request, Response[AppEnv, Throwable]]

  protected val rootPath: Endpoint[Unit, ErrorResponse, Unit, Any] = sttp.tapir.endpoint
    .in("api")
    .tag(info.tagName)
    .errorOut {
      oneOf[ErrorResponse](
        oneOfMapping(StatusCode.BadRequest, jsonBody[ApiService.BadRequest].description("bad request")),
        oneOfMapping(StatusCode.Unauthorized, jsonBody[ApiService.AuthError].description("unauthorized")),
//        oneOfMapping(StatusCode.NotFound, jsonBody[ApiService.NotFound].description("not found")),
//        oneOfMapping(StatusCode.InternalServerError, jsonBody[ApiService.InternalError].description("Internal error")),
//        oneOfMapping(StatusCode.Conflict, jsonBody[ApiService.Conflict].description("Internal error")),
//        oneOfDefaultMapping(jsonBody[ErrorResponse].description("unknown"))
      )
    }
}
