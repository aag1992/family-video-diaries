name := """family-video-diaries"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, SbtWeb)

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  javaWs,
  ws,
  ehcache,
  guice
)

libraryDependencies ++= Vector(
  "org.apache.ivy" % "ivy" % "2.4.0",
  "com.google.cloud" % "google-cloud-storage" % "1.38.0"
)

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.313"
libraryDependencies += ("com.google.inject.extensions" % "guice-multibindings" % "4.2.1")
