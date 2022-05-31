package ru.shop_backend.apiservice.services.basket

import ru.shop_backend.apiservice.services.basket.Models._
import ru.shop_backend.apiservice.{ApiService, ApiServiceEnv, RestService, ServiceInfo}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe._
import sttp.tapir.ztapir.{ZServerEndpoint, _}
import zhttp.http.{Http, Request, Response}

object BasketService extends RestService[ApiServiceEnv] {

  override def info: ServiceInfo = ServiceInfo("Basket")

  override def routes: Http[CoreEnv, Throwable, Request, Response[CoreEnv, Throwable]] = routes_

  override protected val services: List[ZServerEndpoint[CoreEnv, _, ApiService.ErrorResponse, _]] = List(
    rootPath.post
      .description("Добавить устройство в корзину")
      .name("basketPost")
      .in("basket")
      .in(jsonBody[BasketDeviceInsert].description("ID пользователя и устройства"))
      .out(jsonBody[BasketDevice])
      .zServerLogic(info => Logic.insDeviceInBasket(info)),
    rootPath.get
      .description("Получить корзину по id пользователя")
      .name("basketGet")
      .in("basket")
      .in(path[Int]("userId"))
      .out(jsonBody[List[BasketContent]].description("Содержимое корзины"))
      .zServerLogic(userId => Logic.getBasketByUserId(userId)),
    rootPath.delete
      .description("Удалить устройство из корзины")
      .name("basketDel")
      .in("basket")
      .in(path[Int]("id"))
      .out(statusCode)
      .zServerLogic(id => Logic.delDevice(id))
  )

}
