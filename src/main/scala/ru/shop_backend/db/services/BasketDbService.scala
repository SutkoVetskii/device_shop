package ru.shop_backend.db.services

import doobie.implicits.toSqlInterpolator
import ru.shop_backend.apiservice.services.basket.Models._
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.DbConnect.DbConnect
import zio.logging.Logging
import zio.{Has, RIO, ZIO, ZLayer}

object BasketDbService {

  type BasketDbService = Has[Service]

  trait Service {

    def insert(id: Int): RIO[Logging, Basket]

    def insertDevice(basketId: Int, deviceId: Int): ZIO[Logging, Throwable, BasketDevice]

    def getByUserId(userId: Int): RIO[Logging, Basket]

    def getBasContByUserId(userId: Int): RIO[Logging, List[BasketContent]]

    def delDevice(id: Int): ZIO[Logging, Throwable, Int]

  }

  class BasketDbServiceImpl(db: DbConnect.Service) extends Service {

    def insert(id: Int): RIO[Logging, Basket] = db.queryUniqueRet[Basket](
      sql"""insert into basket (user_id) values ($id) returning *"""
    )

    def insertDevice(basketId: Int, deviceId: Int): ZIO[Logging, Throwable, BasketDevice] = db.queryUniqueRet[BasketDevice](
      sql"""insert into basket_device (basket_id, device_id) values ($basketId, $deviceId) returning *"""
    )

    def getByUserId(userId: Int): RIO[Logging, Basket] = db.selectOne[Basket](
      sql"""select * from basket where user_id=$userId;"""
    )

    def getBasContByUserId(userId: Int): RIO[Logging, List[BasketContent]] = db.select[BasketContent](
      sql"""select bd.id, d.name, b.name, t.name, d.price
            from basket_device bd
                     left join device d on d.id = bd.device_id
                     left join brand b on d.brand_id = b.id
                     left join type t on d.type_id = t.id
                     left join basket bas on bd.basket_id = bas.id
            where bas.user_id = $userId"""
    )

    def delDevice(id: Int): ZIO[Logging, Throwable, Int] = db.delete(
      sql"""delete from basket_device where id=$id;"""
    )

  }

  val live: ZLayer[DbConnect, Nothing, BasketDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new BasketDbServiceImpl(db)).toLayer
}
