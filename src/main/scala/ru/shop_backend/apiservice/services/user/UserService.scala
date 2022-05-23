package ru.shop_backend.apiservice.services.user

import ru.shop_backend.apiservice.services.user.Models._
import ru.shop_backend.apiservice.{ApiService, ApiServiceEnv, RestService, ServiceInfo}
import sttp.tapir.{ValidationError, Validator}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe._
import sttp.tapir.ztapir.{ZServerEndpoint, _}
import zhttp.http.{Http, Request, Response}


object UserService extends RestService[ApiServiceEnv]{

  override def info: ServiceInfo = ServiceInfo("User")

  override def routes: Http[CoreEnv, Throwable, Request, Response[CoreEnv, Throwable]] = routes_

  override protected val services: List[ZServerEndpoint[CoreEnv, _, ApiService.ErrorResponse, _]] = List(
    rootPath.post
      .description("Регистрация пользователя")
      .name("user_reg")
      .in("user"/"registration")
      .in(jsonBody[UserRegInfo].description("Данные регистрации"))
      .out(jsonBody[User].description("Зарегестрированный пользователь"))
      .zServerLogic({case user => Logic.registration(user) }),

    rootPath.post
      .description("Авторизация пользователя")
      .name("user_login")
      .in("user"/"login")
      .in(
        jsonBody[UserRegInfo]
          .description("Учётные данные")
          .validate(Validator.custom[UserRegInfo](v => {
            if (v.email.nonEmpty) Nil else List(ValidationError.Custom(v, "Login should not be empty"))
          }))
          .validate(Validator.custom[UserRegInfo](v => {
            if (v.password.nonEmpty) Nil else List(ValidationError.Custom(v, "Password should not be empty"))
          }))
      )
      .out(jsonBody[LoginStatus].description("Статус авторизации"))
      .zServerLogic(userInfo => Logic.login(userInfo))
  )

}
