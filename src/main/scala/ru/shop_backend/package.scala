package ru

import ru.shop_backend.apiservice.HasApiService
import ru.shop_backend.config.GlobalCfg.HasConfig
import ru.shop_backend.db.services.BasketDbService.BasketDbService
import ru.shop_backend.db.services.BrandDbService.BrandDbService
import ru.shop_backend.db.services.DeviceDbService.DeviceDbService
import ru.shop_backend.db.services.TypeDbService.TypeDbService
import ru.shop_backend.db.services.UserDbService.UserDbService
import zio.ZIO
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.Console
import zio.logging._

package object shop_backend {
  type SystemEnv = Blocking with Clock with Console

  type ServiceEnv = SystemEnv with HasConfig with Logging

  type AppEnv =
    ServiceEnv with HasApiService with BrandDbService with TypeDbService with DeviceDbService with UserDbService with BasketDbService

  type AppTaskIO[E, A] = ZIO[AppEnv, E, A]

  type AppTask[A] = AppTaskIO[Throwable, A]
}
