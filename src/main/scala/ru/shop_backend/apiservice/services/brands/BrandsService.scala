package ru.shop_backend.apiservice.services.brands

import ru.shop_backend.apiservice.services.brands.Models._
import ru.shop_backend.apiservice.{ApiService, ApiServiceEnv, RestService, ServiceInfo}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe._
import sttp.tapir.ztapir.{ZServerEndpoint, _}
import zhttp.http.{Http, Request, Response}



object BrandsService extends RestService[ApiServiceEnv]{

  override def info: ServiceInfo = ServiceInfo("Brands")

  override def routes: Http[CoreEnv, Throwable, Request, Response[CoreEnv, Throwable]] = routes_

  override protected val services: List[ZServerEndpoint[CoreEnv, _, ApiService.ErrorResponse, _]] = List(
    rootPath.get
      .description("Получить список брендов")
      .name("brands")
      .in("brands")
      .out(jsonBody[List[Brand]].description("Список брендов"))
      .zServerLogic(_ => Logic.getBrands),

    rootPath.post
      .description("Добавить бренд")
      .name("brands")
      .in("brands")
      .in(jsonBody[BrandInsertInfo].description("Информация для добавления бренда"))
      .out(jsonBody[Brand].description("Добавленный бренд"))
      .zServerLogic({case (info) => Logic.addBrand(info.name) })
  )

}
