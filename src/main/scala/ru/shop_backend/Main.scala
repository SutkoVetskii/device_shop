package ru.shop_backend

import ru.shop_backend.apiservice.{ApiService, HasApiService}
import ru.shop_backend.config.GlobalCfg
import ru.shop_backend.server.Server
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.putStrLn
import zio.{ExitCode, ZIO, ZLayer}
import zio.console._
import zio._

object Main extends App {

  val program: ZIO[AppEnv, Throwable, Unit] =
    for {
//      _   <- printSystemParams
//      _   <- log.debug(Server.getMetric).repeat(spaced).forkDaemon
//      _   <- TenantsValidation.restoreTenantsIntegrity.tapError(thr => log.throwable("restoreTenantsIntegrity", thr))
//      _   <- MinIOValidation.checkBucketNotification.tapError(thr => log.throwable("checkBucketNotification", thr))
      srv <- Server.run
    } yield srv

  val layers: ZLayer[SystemEnv, Any, AppEnv] = {
    val sys = Blocking.live ++ Clock.live ++ Console.live
//    val db              = GlobalCfg.live ++ Blocking.live >>> DbConnect.live todo
    sys ++ ApiService.live ++ GlobalCfg.live
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
