package ru.shop_backend.apiservice.services.brands

import doobie.implicits.toSqlInterpolator
import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.brands.Models.Brand
import ru.shop_backend.db.DbConnect
import zio.logging.Logging
import zio.{Has, ZIO}

object Logic {
//  def getBrands= {
//    ZIO(List(Brand(1, "iphone"), Brand(1, "xiaomi"))).orElseFail(BadRequest("kek"))
//  }

  def getBrands: ZIO[Logging with Has[DbConnect.Service], BadRequest, List[Brand]] = {
    for {
      db <- ZIO.service[DbConnect.Service]
      kek <- db.select[Brand](
        sql"""
          select * from brand;
           """).orElseFail(BadRequest("kek"))
    } yield kek
  }

}
