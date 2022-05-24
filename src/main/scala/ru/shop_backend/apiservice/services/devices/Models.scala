package ru.shop_backend.apiservice.services.devices

import io.circe.Codec

object Models {

  implicit val BrandCodec: Codec[Device]                      = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceInsertInfoCodec: Codec[DeviceInsertInfo] = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceInfoCodec: Codec[DeviceInfo]             = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceResponseInfoCodec: Codec[DeviceResponse] = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceFilterCodec: Codec[DeviceFilter]         = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceRetCodec: Codec[DeviceRet]               = io.circe.generic.semiauto.deriveCodec
  implicit val DeviceUpdateInfoCodec: Codec[DeviceUpdateInfo] = io.circe.generic.semiauto.deriveCodec

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
      dType: String,
      brand: String,
      rating: Int,
      img: String
  )

  case class DeviceRet(
      id: Int,
      name: String,
      price: Int,
      dType: Int,
      brand: Int,
      rating: Int,
      img: String
  )

  case class DeviceInsertInfo(
      name: String,
      price: Int,
      rating: Int,
      img: String,
      typeId: Int,
      brandId: Int
  )

  case class DeviceUpdateInfo(
      name: Option[String],
      price: Option[Int],
      rating: Option[Int],
      img: Option[String],
      typeId: Option[Int],
      brandId: Option[Int]
  )

  case class DeviceInfo(
      id: Int,
      title: String,
      description: String
  )

  case class DeviceResponse(
      device: Device,
      info: List[DeviceInfo]
  )

}
