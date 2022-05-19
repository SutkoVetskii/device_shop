package ru.shop_backend.apiservice.services.brands

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.brands.Models.Brand
import ru.shop_backend.db.service.brand.BrandDbService
import zio.logging.Logging
import zio.{Has, ZIO}

object Logic {

    def getBrands: ZIO[Logging with Has[BrandDbService.Service], BadRequest, List[Brand]] = {
      for {
        service <- ZIO.service[BrandDbService.Service]
        brands <- service.getAll.mapError(e => BadRequest(e.getMessage))
      } yield brands
    }

//  def getBrands: ZIO[Logging with Has[DbConnect.Service], BadRequest, List[Brand]] = {
//    for {
//      db <- ZIO.service[DbConnect.Service]
//      brands <- db.select[Brand](
//        sql"""
//          select * from brand;
//           """).mapError(e => BadRequest(e.getMessage))
//    } yield brands
//  }

}
