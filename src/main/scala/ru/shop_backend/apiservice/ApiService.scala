package ru.shop_backend.apiservice

import cats.effect.Clock
import ru.shop_backend.AppEnv
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml._
import zio.{ULayer, ZIO}
import zhttp.http.{Http, Request, Response}
import sttp.tapir.swagger.ziohttp.SwaggerZioHttp
import zio.blocking.Blocking

class ApiService() {

  implicit val clock: Clock[cats.effect.IO] = Clock.create[cats.effect.IO]

  private val swagger: Http[Blocking, Throwable, Request, Response[Blocking, Throwable]] = {
    val ep = servicesApi.flatMap(_.endpoints)
    new SwaggerZioHttp(OpenAPIDocsInterpreter().toOpenAPI(ep, globalInfo).toYaml).route
  }

  val services: Http[AppEnv, Throwable, Request, Response[AppEnv, Throwable]] = {
    servicesApi.map(_.routes).reduce(_ <> _) <> swagger
  }

}

object ApiService {

  val live: ULayer[HasApiService] = ZIO.effectTotal(new ApiService).toLayer

  sealed abstract class ErrorResponse(msg: String = "Internal error", description: String = "") extends Throwable {
    override def toString: String = msg

    override def getMessage: String = toString
  }

  final case class AuthError(info: String) extends ErrorResponse("Access deny", info)

  final case class BadRequest(info: String) extends ErrorResponse("Bad request", info)

  final case class NotFound(info: String) extends ErrorResponse("NotFound", info)

  final case class Conflict(info: String) extends ErrorResponse("Conflict", info)

  final case class InternalError(info: String) extends ErrorResponse(description = info)

  case class ErrorWithInfo(msg: String) extends Throwable {
    override def toString: String = msg

    override def getMessage: String = toString
  }

}
