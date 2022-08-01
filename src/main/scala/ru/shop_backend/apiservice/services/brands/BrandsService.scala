package ru.shop_backend.apiservice.services.brands

import ru.shop_backend.apiservice.services.brands.Models._
import ru.shop_backend.apiservice.{ApiService, ApiServiceEnv, RestService, ServiceInfo}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe._
import sttp.tapir.ztapir.{ZServerEndpoint, _}
import zhttp.http.{Http, Request, Response}

object BrandsService extends RestService[ApiServiceEnv] {

  override def info: ServiceInfo = ServiceInfo("Brands")

  override def routes: Http[CoreEnv, Throwable, Request, Response[CoreEnv, Throwable]] = routes_

  override protected val services: List[ZServerEndpoint[CoreEnv, _, ApiService.ErrorResponse, _]] = List(
    rootPath.get
      .description("Получить список брендов")
      .name("brandsGet")
      .in("brands")
      .out(jsonBody[List[Brand]].description("Список брендов"))
      .zServerLogic(_ => Logic.getBrands),
    rootPath.post
      .description("Добавить бренд")
      .name("brandsPost")
      .in("brands")
      .in(jsonBody[BrandInsertInfo].description("Информация для добавления бренда"))
      .out(jsonBody[Brand].description("Добавленный бренд"))
      .zServerLogic(info => Logic.addBrand(info.name)),
    rootPath.put
      .description("Обновить бренд")
      .name("brandsPut")
      .in("brands")
      .in(path[Int]("id"))
      .in(jsonBody[BrandInsertInfo].description("Информация для обновления бренда"))
      .out(jsonBody[Brand].description("Обновленный бренд"))
      .logicWithAdminAuth({ case (id, info) => Logic.updateBrand(id, info.name) }),
    rootPath.delete
      .description("Удалить бренд")
      .name("brandsDel ")
      .in("brands")
      .in(path[Int]("id"))
      .out(statusCode)
      .zServerLogic(id => Logic.deleteBrand(id))
  )

}
