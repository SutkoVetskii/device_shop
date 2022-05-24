package ru.shop_backend.apiservice.services.types

import ru.shop_backend.apiservice.services.types.Models._
import ru.shop_backend.apiservice.{ApiService, ApiServiceEnv, RestService, ServiceInfo}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe._
import sttp.tapir.ztapir.{ZServerEndpoint, _}
import zhttp.http.{Http, Request, Response}

object TypesService extends RestService[ApiServiceEnv] {

  override def info: ServiceInfo = ServiceInfo("Types")

  override def routes: Http[CoreEnv, Throwable, Request, Response[CoreEnv, Throwable]] = routes_

  override protected val services: List[ZServerEndpoint[CoreEnv, _, ApiService.ErrorResponse, _]] = List(
    rootPath.get
      .description("Получить список типов")
      .name("types")
      .in("types")
      .out(jsonBody[List[Type]].description("Список типов"))
      .zServerLogic(_ => Logic.getTypes),
    rootPath.post
      .description("Добавить тип")
      .name("types")
      .in("types")
      .in(jsonBody[TypeInsertInfo].description("Информация для добавления типа"))
      .out(jsonBody[Type].description("Добавленный бренд"))
      .zServerLogic({ case (info) => Logic.addType(info.name) }),
    rootPath.put
      .description("Обновить тип")
      .name("types")
      .in("types")
      .in(path[Int]("id"))
      .in(jsonBody[TypeInsertInfo].description("Информация для обновления типа"))
      .out(jsonBody[Type].description("Обновленный тип"))
      .zServerLogic({ case (id, info) => Logic.updateType(id, info.name) }),
    rootPath.delete
      .description("Удалить тип")
      .name("types")
      .in("types")
      .in(path[Int]("id"))
      .out(statusCode)
      .zServerLogic(id => Logic.deleteType(id))
  )

}
