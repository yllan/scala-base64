organization := "totoshi"

name := "scala-base64"

version := "1.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.1",
  "commons-io" % "commons-io" % "2.4"
)

