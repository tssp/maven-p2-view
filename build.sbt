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
val logbackVersion= "1.1.3"
val kamonVersion = "0.4.0"

lazy val loggingDependencies = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion
)

lazy val metricDependencies = Seq(
  "io.kamon" %% "kamon-core" % kamonVersion,
  "io.kamon" %% "kamon-log-reporter" % kamonVersion,
  "org.aspectj" % "aspectjweaver" % "1.8.5"
)

lazy val commonDependencies = Seq(

  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
) ++ loggingDependencies ++ metricDependencies

lazy val coreDependencies = commonDependencies ++ metricDependencies

lazy val core = project.in(file("core"))
  .settings(name := "m2p2-core")
  .settings(commonSettings)
  .settings(libraryDependencies ++= coreDependencies)
  .settings(aspectjSettings)
  .settings(javaOptions <++= AspectjKeys.weaverOptions in Aspectj)
  .settings(fork in run := true)



lazy val apiScala = project.in(file("api-scala"))
    .settings(name := "m2p2-scala-api")
    .settings(commonSettings)
    .settings(libraryDependencies ++= commonDependencies)
    .dependsOn(core)

lazy val root = project.in(file(".")).aggregate(core, apiScala)
