import Dependencies.CONFIG
import sbt._

import scala.languageFeature.postfixOps

object Dependencies {

  object V {

    val zio        = "1.0.10"
    val zioHttp    = "1.0.0.0-RC17"
    val tapir      = "0.18.3"
    val circe      = "0.14.1"
    val ziologging = "0.5.11"
    val flyway     = "6.0.8"
    val pureConfig = "0.16.0"
  }

  object ZIO {
    lazy val core = "dev.zio" %% "zio" % V.zio
  }

  object HTTP {
    lazy val zhttp = "io.d11"   %% "zhttp"         % V.zioHttp
    lazy val circe = "io.circe" %% "circe-generic" % V.circe
  }

  object TAPIR {
    lazy val tapir          = "com.softwaremill.sttp.tapir" %% "tapir-core"               % V.tapir
    lazy val tapircirce     = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % V.tapir
    lazy val tapirdocs      = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % V.tapir
    lazy val tapircirceyaml = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % V.tapir

    lazy val tapirZioHttpServer  = "com.softwaremill.sttp.tapir"  %% "tapir-zio-http"                % V.tapir
    lazy val tapirZioHttpSwagger = "com.softwaremill.sttp.tapir"  %% "tapir-swagger-ui-zio-http"     % V.tapir
    lazy val tapirhttpclient     = "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % "2.1.5"

  }

  object DB {
    lazy val flyway = "org.flywaydb" % "flyway-core" % V.flyway
  }

  object CONFIG {
    lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % V.pureConfig
  }

  lazy val globalProjectDeps = Seq(
    ZIO.core,
    HTTP.circe,
    HTTP.zhttp,
    TAPIR.tapir,
    TAPIR.tapircirce,
    TAPIR.tapirdocs,
    TAPIR.tapircirceyaml,
    TAPIR.tapirZioHttpServer,
    TAPIR.tapirZioHttpSwagger,
    TAPIR.tapirhttpclient,
    DB.flyway,
    CONFIG.pureConfig
  )
}
