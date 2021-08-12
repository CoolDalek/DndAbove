name := "DndAbove"

version := "0.1"

Global / scalaVersion := "2.13.6"

lazy val backendShared = (project in file("backend")).settings(
  libraryDependencies ++= Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-refined" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-newtype" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-json-upickle" % "0.19.0-M4",
    "eu.timepit" %% "refined" % "0.9.27",
    "eu.timepit" %% "refined-pureconfig" % "0.9.27",
    "io.estatico" %% "newtype" % "0.4.4",
    "io.monix" %% "monix" % "3.4.0",
    "org.typelevel" %% "cats-effect" % "2.5.1",
    "com.lihaoyi" %% "upickle" % "1.4.0",
    "com.github.pureconfig" %% "pureconfig" % "0.16.0",
    "com.outr" %% "scribe" % "3.5.3",
    "com.outr" %% "scribe-file" % "3.5.5",
    "com.outr" %% "scribe-slf4j" % "3.5.3",
  ),
  scalacOptions ++= Seq(
    "-language:experimental.macros",
    "-Vmacro",
    "-Wmacros:both",
    "-Ymacro-annotations",
  ),
)

lazy val loadBalancer = (project in file("balancer"))
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.macwire" %% "macros" % "2.4.0" % "provided",
    ),
  ).dependsOn(backendShared)