package ru

import ru.shop_backend.apiservice.HasApiService
import ru.shop_backend.config.GlobalCfg.HasConfig
import zio.ZIO
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.Console

package object shop_backend {
  type SystemEnv = Blocking with Clock with Console

  type ServiceEnv = SystemEnv with HasConfig /*with HasDbConnect*/

  type AppEnv = ServiceEnv
    with HasApiService


  type AppTaskIO[E, A] = ZIO[AppEnv, E, A]

  type AppTask[A] = AppTaskIO[Throwable, A]
}
