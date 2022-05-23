package ru.shop_backend.db.services

import doobie.implicits.toSqlInterpolator
import ru.shop_backend.apiservice.services.devices.Models._
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.DbConnect.DbConnect
import zio.logging.Logging
import zio.{Has, RIO, ZIO, ZLayer}
import doobie._

import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.postgres.implicits._
import zio.{RIO, ZIO}
import zio.logging.Logging

object DeviceDbService {

  type DeviceDbService = Has[Service]
  val tableName = fr"device"

  trait Service {
    def find(filter: DeviceFilter): RIO[Logging, List[Device]]
    def getOne(id: Int): RIO[Logging, DeviceResponseInfo]
    def insert(info: DeviceInsertInfo): RIO[Logging, Int]
  }

  class DeviceDbServiceImpl(db: DbConnect.Service) extends Service {

    def find(filter: DeviceFilter): RIO[Logging, List[Device]] = {
      def getAndOptFr(op: Option[_], fr: String): Option[Fragment] = {
        op.map(x => Fragment.const(s"$fr $x"))
      }
      val where = Fragments.whereAndOpt(
        getAndOptFr(filter.priceFrom, "price >="),
        getAndOptFr(filter.priceTo, "price <="),
        getAndOptFr(filter.priceTo, "type_id ="),
        getAndOptFr(filter.priceTo, "brand_id =")
      )
      val query = sql"""select d.id, d.name, d.price, t.name, b.name, d.rating, d.img
                        from $tableName d
                                 left join brand b on b.id = d.brand_id
                                 left join "type" t on t.id = d.type_id""" ++ where
      db.select[Device](query)
    }

    def insert(info: DeviceInsertInfo): RIO[Logging, Int] = {
      val values = fr"${info.name} , ${info.price}, ${info.rating}, ${info.img}, ${info.typeId}, ${info.brandId}"
      db.insert(
        sql"""insert into $tableName (name, price, rating, img, type_id, brand_id)
             values ($values);"""
      )
    }

    def getOne(id: Int): RIO[Logging, DeviceResponseInfo] =
      for {
        device <- db.selectOne[Device](
          sql"""select * from $tableName where id=$id;"""
        )
        info <- db.select[DeviceInfo](
          sql"""select id, title, description from device_info where device_id=$id"""
        )
      } yield DeviceResponseInfo(device, info)

  }

  val live: ZLayer[DbConnect, Nothing, DeviceDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new DeviceDbServiceImpl(db)).toLayer
}
