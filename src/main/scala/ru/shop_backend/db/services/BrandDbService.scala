package ru.shop_backend.db.services

import doobie.implicits.toSqlInterpolator
import ru.shop_backend.apiservice.services.brands.Models.Brand
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.DbConnect.DbConnect
import zio.logging.Logging
import zio.{Has, RIO, ZIO, ZLayer}

object BrandDbService {

  type BrandDbService = Has[Service]
  val tableName = fr"brand"

  trait Service {
    def getAll: RIO[Logging, List[Brand]]
    def insert(name: String): RIO[Logging, Int]
  }

  class BrandDbServiceImpl(db: DbConnect.Service) extends Service {

    def getAll: RIO[Logging, List[Brand]] = db.select[Brand](
      sql"""select * from $tableName;"""
    )

    def insert(name: String): RIO[Logging, Int] = db.insert(
      sql"""insert into $tableName (name)
           |values ($name);""".stripMargin
      )
  }

  val live: ZLayer[DbConnect, Nothing, BrandDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new BrandDbServiceImpl(db)).toLayer
}
