package ru.shop_backend.apiservice.services.user

import io.circe.Codec

object Models {

  implicit val UserCodec: Codec[User]               = io.circe.generic.semiauto.deriveCodec
  implicit val UserRegInfoCodec: Codec[UserRegInfo] = io.circe.generic.semiauto.deriveCodec
  implicit val LoginStatusCodec: Codec[LoginStatus] = io.circe.generic.semiauto.deriveCodec

  case class User(
      id: Int,
      email: String,
      password: String,
      role: String
  )

  case class UserRegInfo(
      email: String,
      password: String
  )

  case class LoginStatus(
      status: Boolean
  )

}
