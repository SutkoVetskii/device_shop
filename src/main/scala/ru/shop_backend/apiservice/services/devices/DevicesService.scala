package ru.shop_backend.apiservice.services.devices

import ru.shop_backend.apiservice.services.devices.Models._
import ru.shop_backend.apiservice.{ApiService, ApiServiceEnv, RestService, ServiceInfo}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe._
import sttp.tapir.ztapir.{ZServerEndpoint, _}
import zhttp.http.{Http, Request, Response}


object DevicesService extends RestService[ApiServiceEnv] {

  override def info: ServiceInfo = ServiceInfo("Devices")

  override def routes: Http[CoreEnv, Throwable, Request, Response[CoreEnv, Throwable]] = routes_

  override protected val services: List[ZServerEndpoint[CoreEnv, _, ApiService.ErrorResponse, _]] = List(
    rootPath.post
      .description("Получить список устройств")
      .name("devices")
      .in("devices" / "find")
      .in(jsonBody[DeviceFilter].description("Информация для добавления устройства"))
      .out(jsonBody[List[Device]].description("Список устройств"))
      .zServerLogic(filter => Logic.getDevices(filter)),
    rootPath.post
      .description("Добавить стройство")
      .name("devices")
      .in("devices")
      .in(jsonBody[DeviceInsertInfo].description("Информация для добавления устройства"))
      .out(jsonBody[DeviceRet].description("Добавленное устройство"))
      .zServerLogic(info => Logic.addDevice(info)),
    rootPath.get
      .description("Получить информацию об устройстве")
      .name("devices")
      .in("devices")
      .in(path[Int]("id"))
      .description("id устройства")
      .out(jsonBody[DeviceResponse].description("Информация об устройстве"))
      .zServerLogic({ case id => Logic.getDeviceWithInfo(id) }),
    rootPath.put
      .description("Обновить устройство")
      .name("devices")
      .in("devices")
      .in(path[Int]("id"))
      .in(jsonBody[DeviceUpdateInfo].description("Данные для обновления устройства"))
      .description("id устройства")
      .out(jsonBody[DeviceRet].description("Информация об устройстве"))
      .zServerLogic({ case (id, info) => Logic.updateDevice(id, info) }),
        rootPath.delete
          .description("Удалить устройство")
          .name("devices")
          .in("devices")
          .in(path[Int]("id")).description("id устройства")
          .out(statusCode)
          .zServerLogic(id => Logic.deleteDevice(id))
  )

}
