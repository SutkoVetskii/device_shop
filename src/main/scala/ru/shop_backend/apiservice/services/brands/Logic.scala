package ru.shop_backend.apiservice.services.brands

import ru.shop_backend.apiservice.ApiService
import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.brands.BrandsService.CoreEnv
import ru.shop_backend.apiservice.services.brands.Models.Brand
import zio.ZIO

object Logic {
  def getBrands: ZIO[Any, BadRequest, List[Brand]] = {
    ZIO(List(Brand("234", "iphone"), Brand("234", "xiaomi"))).orElseFail(BadRequest("kek"))
  }

}
