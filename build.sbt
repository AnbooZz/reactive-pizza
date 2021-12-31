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
  "org.flywaydb" %% "flyway-play" % "7.15.0"
)

//----------[ Setting ]-----------------
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

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= dbDependences)
  .settings(libraryDependencies ++= Seq(guice, filters, caffeine))
