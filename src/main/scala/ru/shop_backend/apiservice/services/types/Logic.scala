package ru.shop_backend.apiservice.services.types

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.types.Models._
import ru.shop_backend.db.services.TypeDbService
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
      id <- service.insert(name).mapError(e => BadRequest(e.getMessage))
    } yield Type(id, name)

}
