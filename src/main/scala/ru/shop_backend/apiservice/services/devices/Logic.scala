package ru.shop_backend.apiservice.services.devices

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.devices.Models._
import ru.shop_backend.apiservice.services.types.Models._
import ru.shop_backend.db.services.DeviceDbService
import zio.logging.Logging
import zio.{Has, ZIO}

object Logic {

  def getDevices(filter: DeviceFilter): ZIO[Logging with Has[DeviceDbService.Service], BadRequest, List[Device]] =
    for {
      service <- ZIO.service[DeviceDbService.Service]
      types  <- service.find(filter).mapError(e => BadRequest(e.getMessage))
    } yield types

  def addDevice(info: DeviceInsertInfo): ZIO[Logging with Has[DeviceDbService.Service], BadRequest, Device] =
    for {
      service <- ZIO.service[DeviceDbService.Service]
      id <- service.insert(info).mapError(e => BadRequest(e.getMessage))
    } yield Device(id, info.name, info.price, info.rating, 1, 1, "kek")

  def getDeviceWithInfo(id: Int): ZIO[Logging with Has[DeviceDbService.Service], BadRequest, DeviceResponseInfo] =
    for {
      service <- ZIO.service[DeviceDbService.Service]
      device <- service.getOne(id).mapError(e => BadRequest(e.getMessage))
    } yield device

}
