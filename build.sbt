scalaVersion := "2.12.4"

name := "kenfos"
organization := "uk.co.kenfos"
version := "1.0"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.12"
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
