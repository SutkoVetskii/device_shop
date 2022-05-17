package ru.shop_backend.config

import ru.shop_backend.config.GlobalCfg.HasConfig
import ru.shop_backend.server.ServerConfig
import zio.{URIO, ZIO}

case class AppConfig(
    server: ServerConfig,
//    dbConfig: DbConnectConfig, todo

)

object AppConfig {
  def get: URIO[HasConfig, AppConfig] = ZIO.environment[HasConfig].flatMap(_.get.getConfig)

}
