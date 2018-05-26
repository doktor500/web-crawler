scalaVersion := "2.12.4"

name := "kenfos"
organization := "uk.co.kenfos"
version := "1.0"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.12"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
