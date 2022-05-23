package ru.shop_backend.apiservice.services.types

import io.circe.Codec

object Models {

  implicit val BrandCodec: Codec[Type] = io.circe.generic.semiauto.deriveCodec
  implicit val BrandInsertInfoCodec: Codec[TypeInsertInfo] = io.circe.generic.semiauto.deriveCodec

  case class Type(
      id: Int,
      name: String
  )

  case class TypeInsertInfo(
      name: String
  )

}
