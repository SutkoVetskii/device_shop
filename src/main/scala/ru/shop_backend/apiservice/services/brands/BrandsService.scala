package ru.shop_backend.apiservice.services.brands

import ru.shop_backend.apiservice.services.brands.Models.Brand
import ru.shop_backend.apiservice.{ApiService, ApiServiceEnv, RestService, ServiceInfo}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.ztapir.{ZServerEndpoint, _}
import zhttp.http.{Http, Request, Response}


object BrandsService extends RestService[ApiServiceEnv]{

  override def info: ServiceInfo = ServiceInfo("Brands")

  override def routes: Http[CoreEnv, Throwable, Request, Response[CoreEnv, Throwable]] = routes_

  override protected val services: List[ZServerEndpoint[CoreEnv, _, ApiService.ErrorResponse, _]] = List(
    rootPath.get
      .description("Получить список звонков")
      .name("brands")
      .in("brands")
      .out(jsonBody[List[Brand]].description("Список звонков"))
      .zServerLogic(_ => {
        Logic.getBrands
      })
  )

}
