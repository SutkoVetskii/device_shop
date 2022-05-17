package ru.shop_backend.apiservice.services.brands

import io.circe.Codec

object Models {

  implicit val BrandCodec: Codec[Brand] = io.circe.generic.semiauto.deriveCodec

  case class Brand(
      id: String,
      name: String
  )

}