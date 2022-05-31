package ru.shop_backend.apiservice.services.basket

import io.circe.Codec

object Models {

  implicit val BasketCodec: Codec[Basket]                         = io.circe.generic.semiauto.deriveCodec
  implicit val BasketDeviceCodec: Codec[BasketDevice]             = io.circe.generic.semiauto.deriveCodec
  implicit val BasketDeviceInsertCodec: Codec[BasketDeviceInsert] = io.circe.generic.semiauto.deriveCodec
  implicit val BasketResponseCodec: Codec[BasketContent] = io.circe.generic.semiauto.deriveCodec

  case class Basket(
      id: Int,
      userId: Int
  )

  case class BasketDevice(
      id: Int,
      basketId: Int,
      deviceId: Int
  )

  case class BasketDeviceInsert(
      userId: Int,
      deviceId: Int
  )

  case class BasketContent(
      id: Int,
      name: String,
      brand: String,
      dType: String,
      price: Int
  )

}
