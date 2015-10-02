name := "maven-p2-view"

val commonSettings = Seq(
  organization := "io.coding-me",
  scalaVersion := "2.11.7",
  version := "0.1-SNAPSHOT",
//  publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository"))),
//  publishMavenStyle := true,
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
  "io.kamon" %% "kamon-log-reporter" % kamonVersion % "test",
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
  .settings(fork in Test := true)

lazy val nexus2xPlugin = project.in(file("plugin-nexus-2.x"))
  .settings(name := "m2p2-nexus2x")
  .settings(commonSettings)
  .settings(externalPom())
  .settings(javacOptions ++= Seq("-source", "1.7", "-Xlint:deprecation"))
  .settings(resolvers += "Sonatype" at "https://repository.sonatype.org/content/groups/forge/")
  .dependsOn(core)

lazy val root = project.in(file("."))
  //.settings(commonSettings)
  //.aggregate(core, nexus2xPlugin)
  .aggregate(core)
