package ru.shop_backend.apiservice.services.devices

import io.circe.Codec

object Models {

  implicit val BrandCodec: Codec[Device]                          = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceInsertInfoCodec: Codec[DeviceInsertInfo]     = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceInfoCodec: Codec[DeviceInfo]                 = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceResponseInfoCodec: Codec[DeviceResponseInfo] = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceFilterInfoCodec: Codec[DeviceFilter] = io.circe.generic.semiauto.deriveCodec

  case class DeviceResponse(

                           )

  case class DeviceFilter(
      priceFrom: Option[Int],
      priceTo: Option[Int],
      typeId: Option[Int],
      brandId: Option[Int]
  )

  case class Device(
      id: Int,
      name: String,
      price: Int,
      dType: Int,
      brand: Int,
      rating: Int,
      img: String,
  )

  case class DeviceInsertInfo(
      name: String,
      price: Int,
      rating: Int,
      img: String,
      typeId: Int,
      brandId: Int
  )

  case class DeviceInfo(
      id: Int,
      title: String,
      description: String
  )

  case class DeviceResponseInfo(
      device: Device,
      info: List[DeviceInfo]
  )

}
