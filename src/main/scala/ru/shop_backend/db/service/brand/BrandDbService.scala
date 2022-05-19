package ru.shop_backend.db.service.brand

import doobie.implicits.toSqlInterpolator
import ru.shop_backend.apiservice.services.brands.Models.Brand
import zio.logging.Logging
import zio.{Has, RIO, ZIO, ZLayer}
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.DbConnect.DbConnect

object BrandDbService {

  type BrandDbService = Has[Service]

  trait Service {
    def getAll: RIO[Logging, List[Brand]]
  }

  class BrandDbServiceImpl(db: DbConnect.Service) extends Service {

    def getAll: RIO[Logging, List[Brand]] = for {
      brands <-  db.select[Brand](
        sql"""select * from brand;"""
      )
    } yield brands

  }

  val live: ZLayer[DbConnect, Nothing, BrandDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new BrandDbServiceImpl(db)).toLayer
}
