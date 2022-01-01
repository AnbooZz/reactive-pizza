import com.typesafe.sbt.packager.docker._
import com.typesafe.sbt.packager.docker.DockerChmodType.UserGroupWriteExecute
import play.sbt.routes.RoutesKeys

RoutesKeys.routesImport := Seq.empty
//-------[ Project Information ]------------
name                     := "reactive-pizza"
description              := "An Example for N-tiers layer"
ThisBuild / version      := "1.0.0"
ThisBuild / scalaVersion := "2.13.7"
//----------[ Dependences ]-------------
val dbDependences = Seq(
  "mysql" % "mysql-connector-java" % "8.0.27",
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "org.flywaydb" %% "flyway-play" % "7.18.0"
)

//----------[ Common Setting ]-----------------
val commonSettings = List(
  scalacOptions ++= List(
    "-encoding", "utf8",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Ywarn-dead-code",
    "-opt:l:inline",
    "-Xfatal-warnings",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
    "-language:postfixOps"
  ),
  resolvers ++= List(
    Resolver.jcenterRepo,
    Resolver.sonatypeRepo("snapshots")
  )
)

//------------[ Docker setting ]---------------------------
packageName        := "reactive-pizza"
version            := "1.0.0"
maintainer         := "anboo33"
dockerBaseImage    := "openjdk:11"
dockerExposedPorts := Seq(9000, 9000)
daemonUser         := "root"
dockerChmodType    := UserGroupWriteExecute

//------------[ Module setting ]---------------------------
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(DockerPlugin)
  .disablePlugins(PlayLayoutPlugin)
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= dbDependences)
  .settings(libraryDependencies ++= Seq(guice, filters, caffeine))
