package ru.shop_backend.apiservice.services.basket

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.basket.Models._
import ru.shop_backend.db.services.BasketDbService
import sttp.model.StatusCode
import zio.logging.Logging
import zio.{Has, ZIO}

object Logic {

  def insDeviceInBasket(info: BasketDeviceInsert): ZIO[Logging with Has[BasketDbService.Service], BadRequest, BasketDevice] =
    for {
      basketDb <- ZIO.service[BasketDbService.Service]
      basketDevice <- basketDb
        .getByUserId(info.userId)
        .flatMap(basket => basketDb.insertDevice(basket.id, info.deviceId))
        .mapError(e => BadRequest(e.getMessage))
    } yield basketDevice

  def getBasketByUserId(userId: Int): ZIO[Logging with Has[BasketDbService.Service], BadRequest, List[BasketContent]] =
    for {
      basketDb <- ZIO.service[BasketDbService.Service]
      basket <- basketDb.getBasContByUserId(userId)
        .mapError(e => BadRequest(e.getMessage))
    } yield basket

  def delDevice(id: Int) = for {
    basketDb <- ZIO.service[BasketDbService.Service]
    _ <- basketDb.delDevice(id).mapError(e => BadRequest(e.getMessage))
  } yield StatusCode.Ok

//  def addBrand(name: String): ZIO[Logging with Has[BrandDbService.Service], BadRequest, Brand] =
//    for {
//      service <- ZIO.service[BrandDbService.Service]
//      brand   <- service.insert(name).mapError(e => BadRequest(e.getMessage))
//    } yield brand
//
//  def updateBrand(id: Int, name: String): ZIO[Logging with Has[BrandDbService.Service], BadRequest, Brand] =
//    for {
//      service <- ZIO.service[BrandDbService.Service]
//      brand   <- service.update(id, name).mapError(e => BadRequest(e.getMessage))
//    } yield brand
//
//  def deleteBrand(id: Int): ZIO[Logging with Has[BrandDbService.Service], BadRequest, StatusCode] =
//    for {
//      service <- ZIO.service[BrandDbService.Service]
//      _   <- service.delete(id).mapError(e => BadRequest(e.getMessage))
//    } yield StatusCode.Ok

}
