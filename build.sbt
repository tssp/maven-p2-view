name := "maven-p2-view"

val commonSettings = Seq(
  organization := "io.coding-me",
  scalaVersion := "2.11.7",
  version := "0.1-SNAPSHOT",
  scalacOptions := Seq(
    "-language:implicitConversions",
    "-language:postfixOps",
    "-feature",
    "-unchecked",
    "-deprecation",
    "-encoding", "utf8")
)

val akkaVersion= "2.3.12"

lazy val coreDependencies = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )

lazy val core = project.in(file("core"))
  .settings(name := "m2p2-core")
  .settings(commonSettings)
  .settings(libraryDependencies ++= coreDependencies)

lazy val apiScala = project.in(file("api-scala"))
    .settings(name := "m2p2-scala-api")
    .settings(commonSettings)
    .settings(libraryDependencies ++= coreDependencies)
    .dependsOn(core)

lazy val root = project.in(file(".")).aggregate(core, apiScala)
