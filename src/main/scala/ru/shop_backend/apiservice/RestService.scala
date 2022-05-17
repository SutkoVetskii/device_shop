package ru.shop_backend.apiservice

import ru.shop_backend.apiservice.ApiService.{BadRequest, ErrorResponse, ErrorWithInfo, InternalError}
import sttp.tapir._
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zhttp.http.{Http, Request, Response}
import zio.ZIO

trait RestService[R <: ApiServiceEnv] extends RestServiceCore {

  type CoreEnv             = R
  type RestTask[A]         = ZIO[CoreEnv, Throwable, A]
  type RestTaskSpec[R1, A] = ZIO[CoreEnv with R1, Throwable, A]

  protected val services: List[ZServerEndpoint[CoreEnv, _, ErrorResponse, _]]

  protected val paging: EndpointInput[(Int, Int)] =
    query[Int]("page")
      .description("Номер страницы, 0+ ")
      .validate(Validator.custom[Int](v => {
        if (v >= 0) List.empty
        else List(ValidationError.Custom(v, "Номер страницы должен быть положительным числом"))
      }))
      .and(
        query[Int]("limit")
          .description("Лимит записей на страницу [5,100]")
          .validate(Validator.custom[Int](v => {
            if (v >= 5 && v <= 100) List.empty
            else List(ValidationError.Custom(v, "Лимит должен быть в диапазоне от 5 до 100"))
          }))
      )

//  protected val tenant = path[String]("tenant").description("Идентификатор организации")todo

  override def endpoints: Iterable[Endpoint[_, _, _, _]] = services.map(_.endpoint)

  protected def routes_ : Http[CoreEnv, Throwable, Request, Response[CoreEnv, Throwable]] = {
    services.map(ZioHttpInterpreter().toHttp(_)).reduce(_ <> _)
  }

  implicit def handleError(err: Throwable): ErrorResponse = err match {
//    case err: HttpClientError =>
//      InternalError(err.msg)
//    case err: AuthProviderException =>
//      AuthError(err.getMessage)todo
    case err: ErrorWithInfo =>
      BadRequest(err.msg)
    case err: ErrorResponse =>
      err
    case _ =>
      InternalError("unknown")
  }

}
