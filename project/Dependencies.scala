import sbt._

import scala.languageFeature.postfixOps

object Dependencies {

  object V {

    val zio              = "1.0.10"
    val zioHttp          = "1.0.0.0-RC17"
    val tapir            = "0.18.3"
    val circe            = "0.14.1"
    val ziologging       = "0.5.11"
    val flyway           = "8.5.10"
    val pureConfig       = "0.16.0"
    val doobie       = "0.13.4"


    val LiquibaseVersion = "3.4.2"
    val quil             = "3.12.0"
  }

  object ZIO {
    lazy val core = "dev.zio" %% "zio" % V.zio
    lazy val macros = "dev.zio" %% "zio-macros" % V.zio
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
    lazy val flyway    = "org.flywaydb"  % "flyway-core"     % V.flyway
    lazy val core      = "org.tpolecat" %% "doobie-core"      % V.doobie
    lazy val postgres  = "org.tpolecat" %% "doobie-postgres"  % V.doobie
    lazy val hikari    = "org.tpolecat" %% "doobie-hikari"    % V.doobie

  }

  object LOGS {
    lazy val ziologging      = "dev.zio"                    %% "zio-logging"       % V.ziologging
  }

  object CONFIG {
    lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % V.pureConfig
  }

  lazy val globalProjectDeps = Seq(
    ZIO.core,
    ZIO.macros,
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
    DB.core,
    DB.hikari,
    DB.postgres,
    CONFIG.pureConfig,
    LOGS.ziologging,
  )
}
