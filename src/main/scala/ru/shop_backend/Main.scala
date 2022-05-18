package ru.shop_backend

import ru.shop_backend.apiservice.{ApiService, HasApiService}
import ru.shop_backend.config.GlobalCfg
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.DbConnect.HasDbConnect
import ru.shop_backend.server.Server
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.putStrLn
import zio.{ExitCode, ZIO, ZLayer}
import zio.console._
import zio._
import zio.duration.durationInt
import zio.logging._
import scala.jdk.CollectionConverters._


object Main extends App {

  val program: ZIO[AppEnv, Throwable, Unit] =
    for {
      srv <- Server.run
    } yield srv

  val layers: ZLayer[SystemEnv, Throwable, AppEnv] = {
    val sys = Blocking.live ++ Clock.live ++ Console.live
    val db  = GlobalCfg.live ++ Blocking.live >>> DbConnect.live
    val log = Logging.console(
      logLevel = LogLevel.Info,
      format = LogFormat.ColoredLogFormat()
    ) >>> Logging.withRootLoggerName("my-component")

    sys ++ ApiService.live ++ db ++ GlobalCfg.live ++ log
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    program
      .provideLayer(layers)
      .foldM(
        err => putStrLn(s"Execution failed with: $err").exitCode,
        _ => ZIO.succeed(ExitCode.success)
      )
  }

}
