package ru.shop_backend.server

import cats.effect._
import ru.shop_backend.{AppEnv, AppTask}
import ru.shop_backend.apiservice.HasApiService
import ru.shop_backend.config.AppConfig
import zhttp.http.{CORS, CORSConfig, Method}
import zhttp.service.server.ServerChannelFactory
import zhttp.service.{EventLoopGroup, Server => zServer}
import zio._
import zio.logging.log

case class ServerConfig(
    port: Int,
    address: String
)

object Server {
  implicit val clock: Clock[cats.effect.IO] = Clock.create[cats.effect.IO]

  val cORSConfig: CORSConfig = CORSConfig(
    anyOrigin = true,
    anyMethod = true,
    allowedOrigins = Set("*"),
    allowedMethods = Some(Set(Method.GET, Method.POST, Method.PUT, Method.OPTIONS)),//todo
    allowCredentials = true
  )

  val run: AppTask[Unit] = {
    for {
      config <- AppConfig.get
      route  <- ZIO.access[HasApiService](_.get.services)
      server = zServer.port(config.server.port) ++
        zServer.maxRequestSize(100 * 1020 * 1024) ++ //100 Mb
        zServer.app(CORS(route, cORSConfig)) ++
        zServer.error(thr => log.throwable("ZIO-HTTP", thr))
      _ <- server.make.useForever
        .provideSomeLayer[AppEnv](ServerChannelFactory.auto ++ EventLoopGroup.auto(0))
        .exitCode
    } yield ()
  }
}
