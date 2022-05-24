package ru.shop_backend.apiservice.services.user

import ru.shop_backend.apiservice.ApiService.BadRequest
import ru.shop_backend.apiservice.services.user.Models._
import ru.shop_backend.db.services.UserDbService
import zio.logging.Logging
import zio.{Has, RIO, ZIO}

object Logic {

  def registration(user: UserRegInfo): ZIO[Logging with Has[UserDbService.Service], BadRequest, User] =
    for {
      service <- ZIO.service[UserDbService.Service]
      user <- service.insert(user)
        .mapError(e => BadRequest(e.getMessage))
    } yield user

  def login(userInfo: UserRegInfo): ZIO[Logging with Has[UserDbService.Service], BadRequest, LoginStatus] =
    for {
      service <- ZIO.service[UserDbService.Service]
      user <- service.find(userInfo.email).mapError(e => BadRequest(e.getMessage))
      ret = if (userInfo.password == user.password) LoginStatus(true) else LoginStatus(false)
    } yield ret

}
