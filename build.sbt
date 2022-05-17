name := "shop_backend"

version := "0.1"

scalaVersion := "2.13.8"

def scalafmtSettings = Seq(
  Compile / compile := (Compile / compile)
    .dependsOn(
      Compile / scalafmtCheckAll,
      Compile / scalafmtSbtCheck
    )
    .value
)

lazy val root = (project in file("."))
  .settings(
    Compile / doc / sources := Seq.empty,
    libraryDependencies ++= Dependencies.globalProjectDeps,
    scalafmtSettings
  )