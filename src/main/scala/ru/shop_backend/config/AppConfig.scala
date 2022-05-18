package ru.shop_backend.config

import ru.shop_backend.config.GlobalCfg.HasConfig
import ru.shop_backend.db.DbConnect.DbConnectConfig
//import ru.shop_backend.db12.dbConnection.{DbConnectConfig, LiquibaseConfig}
import ru.shop_backend.server.ServerConfig
import zio.{URIO, ZIO}

case class AppConfig(
    server: ServerConfig,
    dbConfig: DbConnectConfig,
//    liquibase: LiquibaseConfig

)

object AppConfig {
  def get: URIO[HasConfig, AppConfig] = ZIO.environment[HasConfig].flatMap(_.get.getConfig)

}
