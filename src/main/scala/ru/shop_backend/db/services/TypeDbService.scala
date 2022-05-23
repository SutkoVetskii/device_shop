package ru.shop_backend.db.services

import doobie.implicits.toSqlInterpolator
import ru.shop_backend.apiservice.services.types.Models._
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.DbConnect.DbConnect
import zio.logging.Logging
import zio.{Has, RIO, ZIO, ZLayer}

object TypeDbService {

  type TypeDbService = Has[Service]
  val tableName = fr"type"

  trait Service {
    def getAll: RIO[Logging, List[Type]]
    def insert(name: String): RIO[Logging, Int]
  }

  class TypeDbServiceImpl(db: DbConnect.Service) extends Service {

    def getAll: RIO[Logging, List[Type]] = db.select[Type](
      sql"""select * from $tableName;"""
    )

    def insert(name: String): RIO[Logging, Int] = db.insert(
      sql"""insert into $tableName (name)
           |values ($name);""".stripMargin
    )
  }

  val live: ZLayer[DbConnect, Nothing, TypeDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new TypeDbServiceImpl(db)).toLayer
}
