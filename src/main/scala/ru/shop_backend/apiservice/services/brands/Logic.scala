package ru.shop_backend.apiservice.services.brands

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.brands.Models.Brand
import ru.shop_backend.db.services.BrandDbService
import zio.logging.Logging
import zio.{Has, ZIO}

object Logic {

  def getBrands: ZIO[Logging with Has[BrandDbService.Service], BadRequest, List[Brand]] =
    for {
      service <- ZIO.service[BrandDbService.Service]
      brands  <- service.getAll.mapError(e => BadRequest(e.getMessage))
    } yield brands

  def addBrand(name: String): ZIO[Logging with Has[BrandDbService.Service], BadRequest, Brand] =
    for {
      service <- ZIO.service[BrandDbService.Service]
      id <- service.insert(name).mapError(e => BadRequest(e.getMessage))
    } yield Brand(id, name)

}
