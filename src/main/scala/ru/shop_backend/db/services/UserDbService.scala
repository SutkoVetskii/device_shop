package ru.shop_backend.db.services

import doobie.implicits.toSqlInterpolator
import ru.shop_backend.apiservice.services.user.Models._
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.DbConnect.DbConnect
import zio.logging.Logging
import zio.{Has, RIO, ZIO, ZLayer}

object UserDbService {

  type UserDbService = Has[Service]
  val tableName = fr""""user""""

  trait Service {
    def insert(user: UserRegInfo): RIO[Logging, Int]
    def find(id: Int): RIO[Logging, User]
    def find(email: String): RIO[Logging, User]
  }

  class UserDbServiceImpl(db: DbConnect.Service) extends Service {

    def insert(user: UserRegInfo): RIO[Logging, Int] = {
      val values = fr"${user.email}, ${user.password}"
      db.insert(
        sql"""insert into $tableName (email, password)
             |values ($values);""".stripMargin
      )
    }

    def find(id: Int): RIO[Logging, User] = db.selectOne[User](
      sql"""select * from $tableName where id=$id;"""
    )

    def find(email: String): RIO[Logging, User] = db.selectOne[User](
      sql"""select * from $tableName where email=$email;"""
    )

  }

  val live: ZLayer[DbConnect, Nothing, UserDbService] = (for {
    db <- ZIO.service[DbConnect.Service]
  } yield new UserDbServiceImpl(db)).toLayer
}
