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
    def insert(name: String): RIO[Logging, Brand]
    def update(id: Int, name: String): ZIO[Logging, Throwable, Brand]
    def delete(id: Int): ZIO[Logging, Throwable, Int]
  }

  class BrandDbServiceImpl(db: DbConnect.Service) extends Service {

    def getAll: RIO[Logging, List[Brand]] = db.select[Brand](
      sql"""select * from $tableName;"""
    )

    def insert(name: String): RIO[Logging, Brand] = db.queryUniqueRet[Brand](
      sql"""insert into $tableName (name)
                   values ($name) returning *;"""
    )

    def update(id: Int, name: String): ZIO[Logging, Throwable, Brand] = db.queryUniqueRet[Brand](
      sql"""update $tableName set name = $name where id=$id returning *;"""
    )

    def delete(id: Int): ZIO[Logging, Throwable, Int] = db.delete(
      sql"""delete from $tableName where id=$id;"""
    )
  }

  val live: ZLayer[DbConnect, Nothing, BrandDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new BrandDbServiceImpl(db)).toLayer
}
