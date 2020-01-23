name := """ASK HR MANAGEMENT MongoDB"""
organization := "com.example"
version := "1.0-SNAPSHOT"
lazy val root = (project in file(".")).enablePlugins(PlayScala)
scalaVersion := "2.12.8"
libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test

//libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.18.3-play26"
play.sbt.routes.RoutesKeys.routesImport +=
  "play.modules.reactivemongo.PathBindables._"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.18.4-play27"
)