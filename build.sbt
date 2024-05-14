import sbtassembly.AssemblyPlugin.autoImport.assembly

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .enablePlugins(AssemblyPlugin)
  .settings(
    name := "RedisToolBox",
    assembly / assemblyJarName := "RedisToolBox.jar",
    assembly / mainClass := Some("Main"),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    },
    libraryDependencies ++= Seq(
      "redis.clients" % "jedis" % "5.1.2",
      "me.tongfei" % "progressbar" % "0.10.1"
    ),
  )
