package ru.shop_backend.db.services

import doobie.implicits.toSqlInterpolator
import ru.shop_backend.apiservice.services.devices.Models._
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.DbConnect.DbConnect
import zio.logging.Logging
import zio.{Has, RIO, ZIO, ZLayer}
import doobie._

object DeviceDbService {

  type DeviceDbService = Has[Service]
  val tableName = fr"device"

  trait Service {
    def find(filter: DeviceFilter): RIO[Logging, List[Device]]
    def getOne(id: Int): RIO[Logging, DeviceResponse]
    def insert(info: DeviceInsertInfo): RIO[Logging, DeviceRet]
    def update(id: Int, info: DeviceUpdateInfo): ZIO[Logging, Throwable, DeviceRet]
    def delete(id: Int): ZIO[Logging, Throwable, Int]
  }

  class DeviceDbServiceImpl(db: DbConnect.Service) extends Service {

    def find(filter: DeviceFilter): RIO[Logging, List[Device]] = {
      val where = Fragments.whereAndOpt(
        db.getOptFr(filter.priceFrom, "d.price >="),
        db.getOptFr(filter.priceTo, "d.price <="),
        db.getOptFr(filter.priceTo, "d.type_id ="),
        db.getOptFr(filter.priceTo, "d.brand_id =")
      )
      val query = sql"""select d.id, d.name, d.price, t.name, b.name, d.rating, d.img
                        from $tableName d
                                 left join brand b on b.id = d.brand_id
                                 left join "type" t on t.id = d.type_id """ ++ where
      db.select[Device](query)
    }

    def insert(info: DeviceInsertInfo): RIO[Logging, DeviceRet] = {
      val values = fr"${info.name} , ${info.price}, ${info.rating}, ${info.img}, ${info.typeId}, ${info.brandId}"
      db.queryUniqueRet[DeviceRet](
        sql"""insert into $tableName (name, price, rating, img, type_id, brand_id)
             values ($values) returning *;"""
      )
    }

    def update(id: Int, info: DeviceUpdateInfo): ZIO[Logging, Throwable, DeviceRet] = {
      val set = Fragments.setOpt(
        db.getOptFr(info.name, "name ="),
        db.getOptFr(info.price, "price ="),
        db.getOptFr(info.typeId, "type_id ="),
        db.getOptFr(info.brandId, "brand_id ="),
        db.getOptFr(info.rating, "rating ="),
        db.getOptFr(info.img, "img ="),
        Some(fr"updated_at = current_timestamp")
      )

      db.queryUniqueRet[DeviceRet](
        sql"""update $tableName $set where id=$id returning *;"""
      )
    }

    def getOne(id: Int): RIO[Logging, DeviceResponse] =
      for {
        device <- db.selectOne[Device](
          sql"""select d.id, d.name, d.price, t.name, b.name, d.rating, d.img
                from $tableName d
                     left join brand b on b.id = d.brand_id
                     left join "type" t on t.id = d.type_id where d.id=$id;"""
        )
        info <- db.select[DeviceInfo](
          sql"""select id, title, description from device_info where device_id=$id"""
        )
      } yield DeviceResponse(device, info)

    def delete(id: Int): ZIO[Logging, Throwable, Int] = db.delete(
      sql"""delete from basket_device where device_id=$id;
                   delete from device_info where device_id=$id;
                   delete from device where id=$id;
         """
    )

  }

  val live: ZLayer[DbConnect, Nothing, DeviceDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new DeviceDbServiceImpl(db)).toLayer
}
