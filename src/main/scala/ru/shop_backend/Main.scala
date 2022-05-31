package ru.shop_backend

import ru.shop_backend.apiservice.ApiService
import ru.shop_backend.config.GlobalCfg
import ru.shop_backend.db.DbConnect
import ru.shop_backend.db.services.{BasketDbService, BrandDbService, DeviceDbService, TypeDbService, UserDbService}
import ru.shop_backend.server.Server
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.{putStrLn, _}
import zio.logging._

object Main extends App {

  val program: ZIO[AppEnv, Throwable, Unit] =
    for {
      srv <- Server.run
    } yield srv

  val layers: ZLayer[SystemEnv, Throwable, AppEnv] = {
    val sys   = Blocking.live ++ Clock.live ++ Console.live
    val db    = GlobalCfg.live ++ Blocking.live >>> DbConnect.live
    val brand = db >>> BrandDbService.live
    val dType = db >>> TypeDbService.live
    val device = db >>> DeviceDbService.live
    val user = db >>> UserDbService.live
    val basket = db >>> BasketDbService.live
    val log = Logging.console(
      logLevel = LogLevel.Info,
      format = LogFormat.ColoredLogFormat()
    ) >>> Logging.withRootLoggerName("my-component")

    sys ++ ApiService.live ++ GlobalCfg.live ++ log ++ brand ++ dType ++ device ++ user ++ basket
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
