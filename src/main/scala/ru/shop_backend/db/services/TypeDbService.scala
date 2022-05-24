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
    def insert(name: String): RIO[Logging, Type]
    def update(id: Int, name: String): ZIO[Logging, Throwable, Type]
    def delete(id: Int): ZIO[Logging, Throwable, Int]
  }

  class TypeDbServiceImpl(db: DbConnect.Service) extends Service {

    def getAll: RIO[Logging, List[Type]] = db.select[Type](
      sql"""select * from $tableName;"""
    )

    def insert(name: String): RIO[Logging, Type] = db.queryUniqueRet[Type](
      sql"""insert into $tableName (name)
            values ($name) returning *;"""
    )

    def update(id: Int, name: String): ZIO[Logging, Throwable, Type] = db.queryUniqueRet[Type](
      sql"""update $tableName set name = $name where id=$id returning *;"""
    )

    def delete(id: Int): ZIO[Logging, Throwable, Int] = db.delete(
      sql"""delete from $tableName where id=$id;"""
    )
  }

  val live: ZLayer[DbConnect, Nothing, TypeDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new TypeDbServiceImpl(db)).toLayer
}
