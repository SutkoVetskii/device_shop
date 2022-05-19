//package ru.shop_backend.db.service.brand
//
//import doobie.implicits._
//import ru.shop_backend.apiservice.services.brands.Models.Brand
//import ru.shop_backend.db.DbConnect
//import ru.shop_backend.db.service.brand.BrandDbService.Service
//import zio.RIO
//import zio.logging.Logging
//
//class BrandDbServiceImpl(db: DbConnect.Service) extends Service {
//
//  def getAll: RIO[Logging, List[Brand]] = for {
//    brands <-  db.select[Brand](
//      sql"""select * from brand;"""
//    )
//  } yield brands
//
//}
