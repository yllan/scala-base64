organization := "totoshi"

name := "scala-base64"

version := "1.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.13" % "test",
  "commons-io" % "commons-io" % "2.4"
)

