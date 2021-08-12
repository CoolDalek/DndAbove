name := "DndAbove"

version := "0.1"

scalaVersion := "2.13.6"

lazy val backendShared = (project in file("backend")).settings(
  libraryDependencies ++= Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-cats" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-refined" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-newtype" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-derevo" % "0.19.0-M4",
    "com.softwaremill.sttp.tapir" %% "tapir-json-upickle" % "0.19.0-M4",
    "eu.timepit" %% "refined" % "0.9.27",
    "eu.timepit" %% "refined-cats" % "0.9.27",
    "eu.timepit" %% "refined-pureconfig" % "0.9.27",
    "com.github.pureconfig" %% "pureconfig" % "0.16.0",
    "tf.tofu" %% "derevo-cats" % "0.12.6",
    "tf.tofu" %% "tofu" % "0.10.3",
    "io.monix" %% "monix" % "3.4.0",
    "io.estatico" %% "newtype" % "0.4.4",
    "org.typelevel" %% "cats-effect" % "2.5.1",
    "org.typelevel" %% "cats-core" % "2.6.1",
    "com.lihaoyi" %% "upickle" % "1.4.0",
    "com.outr" %% "scribe-slf4j" % "3.5.3",
    "com.outr" %% "scribe" % "3.5.3",
    "com.softwaremill.macwire" %% "macros" % "2.4.0" % "provided",
  )
)

lazy val loadBalancer = (project in file("load")).dependsOn(backendShared)