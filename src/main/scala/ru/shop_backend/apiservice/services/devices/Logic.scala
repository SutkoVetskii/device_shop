package ru.shop_backend.apiservice.services.devices

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.devices.Models._
import ru.shop_backend.db.services.DeviceDbService
import sttp.model.StatusCode
import zio.logging.Logging
import zio.{Has, ZIO}

object Logic {

  def getDevices(filter: DeviceFilter): ZIO[Logging with Has[DeviceDbService.Service], BadRequest, List[Device]] =
    for {
      service <- ZIO.service[DeviceDbService.Service]
      types  <- service.find(filter).mapError(e => BadRequest(e.getMessage))
    } yield types

  def addDevice(info: DeviceInsertInfo): ZIO[Logging with Has[DeviceDbService.Service], BadRequest, DeviceRet] =
    for {
      service <- ZIO.service[DeviceDbService.Service]
      device <- service.insert(info).mapError(e => BadRequest(e.getMessage))
    } yield device

  def getDeviceWithInfo(id: Int): ZIO[Logging with Has[DeviceDbService.Service], BadRequest, DeviceResponse] =
    for {
      service <- ZIO.service[DeviceDbService.Service]
      device <- service.getOne(id).mapError(e => BadRequest(e.getMessage))
    } yield device

  def updateDevice(id: Int, info: DeviceUpdateInfo): ZIO[Logging with Has[DeviceDbService.Service], BadRequest, DeviceRet] = {
    for {
      service <- ZIO.service[DeviceDbService.Service]
      device <- service.update(id, info).mapError(e => BadRequest(e.getMessage))
    } yield device
  }

  def deleteDevice(id: Int): ZIO[Logging with Has[DeviceDbService.Service], BadRequest, StatusCode] = for {
    service <- ZIO.service[DeviceDbService.Service]
    _ <- service.delete(id).mapError(e => BadRequest(e.getMessage))
  } yield StatusCode.Ok

}
