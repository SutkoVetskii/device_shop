package ru.shop_backend.db

import cats.effect.Blocker
import doobie._
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.pos.Pos
import org.flywaydb.core.Flyway
import ru.shop_backend.config.AppConfig
import ru.shop_backend.config.GlobalCfg.HasConfig
import zio.blocking.{Blocking, blocking}
import zio.interop.catz._
import zio.logging.{Logging, log}
import zio.{Has, RIO, Task, URIO, ZIO, ZLayer, blocking => _}

object DbConnect {

  type DbConnect = Has[Service]

  case class DbConnectConfig(url: String, user: String, password: String, driver: String)

  case class Service(connect: HikariTransactor[Task]) {

    private def logMsg(command: String, startTime: Long, srcLine: Option[Pos], count: Int = 1): URIO[Logging, String] =
      for {
        t1 <- ZIO.effectTotal {
          System.currentTimeMillis() - startTime
        }
        sql = s"""Start sql :
                 | Line: ${srcLine.map(_.toString).getOrElse("")}
                 | ==================
                 | $command
                 | ==================
                 | Count: $count
                 | Time: $t1 ms""".stripMargin
        _ <- log.debug(sql)
      } yield sql

    def selectOne[O: Read](query: Query0[O]): ZIO[Logging, Throwable, O] =
      for {
        t0 <- ZIO.effectTotal(System.currentTimeMillis())
        ret <- query.unique
          .transact(connect)
          .tapError(ext => log.throwable(query.sql, ext))
        _ <- logMsg(query.sql, t0, query.pos)
      } yield ret

    def selectOne[O: Read](sql: String): RIO[Logging, O] = selectOne(Query0[O](sql, None))

    def selectOne[O: Read](sql: Fragment): RIO[Logging, O] = selectOne(sql.query[O])

    def select[O: Read](query: Query0[O]): ZIO[Logging, Throwable, List[O]] =
      for {
        t0 <- ZIO.effectTotal(System.currentTimeMillis())
        ret <- query.stream.compile.toList
          .transact(connect)
          .tapError(ext => log.throwable(query.sql, ext))
        _ <- logMsg(query.sql, t0, query.pos, ret.size)
      } yield ret

    def select[O: Read](sql: String): RIO[Logging, List[O]] = select(Query0[O](sql, None))

    def select[O: Read](sql: Fragment): RIO[Logging, List[O]] = select(sql.query[O])

    def queryUniqueRet[A: Read](query: Fragment): ZIO[Logging, Throwable, A] = {
      for {
        t0 <- ZIO.effectTotal(System.currentTimeMillis())
        q = query.update
        ret <- query.query[A].unique
          .transact(connect)
          .tapError(expt => log.throwable(q.sql, expt))
        _ <- logMsg(q.sql, t0, q.pos)
      } yield ret
    }

    def delete(query: Fragment): ZIO[Logging, Throwable, Int] = for {
      t0 <- ZIO.effectTotal(System.currentTimeMillis())
      q = query.update
      ret <- q.run.transact(connect)
        .tapError(expt => log.throwable(q.sql, expt))
      _ <- logMsg(q.sql, t0, q.pos, ret)
    } yield ret

    def getOptFr(op: Option[_], fr: String): Option[Fragment] = {
      op.map(x => Fragment.const({
        val value = x match {
          case v: Int => v
          case _ => s"'$x'"
        }
        s"$fr $value"
      }))
    }
  }

  val live: ZLayer[HasConfig with Blocking, Throwable, DbConnect] = ZLayer.fromManaged(
    for {
      liveEC   <- ZIO.descriptor.map(_.executor.asEC).toManaged_
      blockEC  <- blocking(ZIO.descriptor.map(_.executor.asEC)).toManaged_
      dbConfig <- AppConfig.get.map(_.dbConfig).toManaged_
      connect <- HikariTransactor
        .newHikariTransactor[Task](
          dbConfig.driver,
          dbConfig.url,
          dbConfig.user,
          dbConfig.password,
          liveEC,
          Blocker.liftExecutionContext(blockEC)
        )
        .toManagedZIO
      _ <- connect.configure { source =>
        ZIO.effect(
          Flyway
            .configure()
            .dataSource(source)
            .load()
            .migrate()
        )
      }.toManaged_
    } yield Service(connect)
  )

}
