package ru.shop_backend.apiservice.services.types

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.types.Models._
import ru.shop_backend.db.services.TypeDbService
import sttp.model.StatusCode
import zio.logging.Logging
import zio.{Has, ZIO}

object Logic {

  def getTypes: ZIO[Logging with Has[TypeDbService.Service], BadRequest, List[Type]] =
    for {
      service <- ZIO.service[TypeDbService.Service]
      types  <- service.getAll.mapError(e => BadRequest(e.getMessage))
    } yield types

  def addType(name: String): ZIO[Logging with Has[TypeDbService.Service], BadRequest, Type] =
    for {
      service <- ZIO.service[TypeDbService.Service]
      t <- service.insert(name).mapError(e => BadRequest(e.getMessage))
    } yield t

  def updateType(id: Int, name: String): ZIO[Logging with Has[TypeDbService.Service], BadRequest, Type] =
    for {
      service <- ZIO.service[TypeDbService.Service]
      brand   <- service.update(id, name).mapError(e => BadRequest(e.getMessage))
    } yield brand

  def deleteType(id: Int): ZIO[Logging with Has[TypeDbService.Service], BadRequest, StatusCode] =
    for {
      service <- ZIO.service[TypeDbService.Service]
      _   <- service.delete(id).mapError(e => BadRequest(e.getMessage))
    } yield StatusCode.Ok

}
