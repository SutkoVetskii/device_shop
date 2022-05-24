package ru.shop_backend.apiservice.services.brands

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.brands.Models.Brand
import ru.shop_backend.db.services.BrandDbService
import sttp.model.StatusCode
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
      brand   <- service.insert(name).mapError(e => BadRequest(e.getMessage))
    } yield brand

  def updateBrand(id: Int, name: String): ZIO[Logging with Has[BrandDbService.Service], BadRequest, Brand] =
    for {
      service <- ZIO.service[BrandDbService.Service]
      brand   <- service.update(id, name).mapError(e => BadRequest(e.getMessage))
    } yield brand

  def deleteBrand(id: Int): ZIO[Logging with Has[BrandDbService.Service], BadRequest, StatusCode] =
    for {
      service <- ZIO.service[BrandDbService.Service]
      _   <- service.delete(id).mapError(e => BadRequest(e.getMessage))
    } yield StatusCode.Ok

}
