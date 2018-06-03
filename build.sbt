scalaVersion := "2.12.6"

name := "crawler"
organization := "uk.co.kenfos"
version := "1.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.12"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
libraryDependencies += "commons-validator" % "commons-validator" % "1.6"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.5"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.5"
libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.13.3"
libraryDependencies += "org.codehaus.groovy" % "groovy" % "2.5.0"
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test

assemblyJarName in assembly := "crawler.jar"

assemblyMergeStrategy in assembly := {
  case file if file.toLowerCase.endsWith("manifest.mf")         => MergeStrategy.discard
  case file if file.toLowerCase.endsWith("versions.properties") => MergeStrategy.discard
  case file                                                     => (assemblyMergeStrategy in assembly).value(file)
}