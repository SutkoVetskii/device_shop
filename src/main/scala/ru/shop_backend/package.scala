package ru

import ru.shop_backend.apiservice.HasApiService
import ru.shop_backend.config.GlobalCfg.HasConfig
import ru.shop_backend.db.DbConnect.DbConnect
import ru.shop_backend.db.service.brand.BrandDbService.BrandDbService
import zio.ZIO
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.Console
import zio.logging._


package object shop_backend {
  type SystemEnv = Blocking with Clock with Console

  type ServiceEnv = SystemEnv with HasConfig with Logging

  type AppEnv = ServiceEnv with HasApiService with BrandDbService

  type AppTaskIO[E, A] = ZIO[AppEnv, E, A]

  type AppTask[A] = AppTaskIO[Throwable, A]
}
