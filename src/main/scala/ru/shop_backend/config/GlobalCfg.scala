package ru.shop_backend.config

import zio._

object GlobalCfg {

  type HasConfig = Has[Service]

  trait Service {
    def getConfig: UIO[AppConfig]
  }

  def fromConfig(appConfig: AppConfig): Service = new Service {
    override def getConfig: UIO[AppConfig] = ZIO.succeed(appConfig)
  }

  val live: ULayer[HasConfig] = {
    def impureConfig: AppConfig = {
      import pureconfig._
      import pureconfig.generic.auto._

      ConfigSource.default.loadOrThrow[AppConfig]
    }

    ZIO.effect(impureConfig).map(fromConfig).orDie
  }.toLayer
}
