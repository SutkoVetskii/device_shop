package ru.shop_backend

import ru.shop_backend.apiservice.services.brands.BrandsService
import ru.shop_backend.apiservice.services.devices.DevicesService
import ru.shop_backend.apiservice.services.types.TypesService
import ru.shop_backend.apiservice.services.user.UserService
import sttp.tapir.openapi.{Contact, Info}
import zio.{Has, ZIO}

package object apiservice {

  type HasApiService = Has[ApiService]

  type ApiServiceEnv = AppEnv /*with HasAuthProvider*/

  type ApiServiceEffect[A] = ZIO[ApiServiceEnv, Throwable, A]

  val globalInfo: Info = Info(
    "DeviceShop",
    "1.0",
    Some("API backend"),
    None,
    Some(Contact(Some("Sutkovetskiy Sergey"), Some("ssutkovetskiy@beeline.ru"), None)),
    None
  )

  val servicesApi: List[RestServiceCore] = List(
    BrandsService,
    TypesService,
    DevicesService,
    UserService
  )
}
