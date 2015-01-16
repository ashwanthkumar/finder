import sbt.Keys._
import sbt._

object Build extends Build {

  val AppVersion = "1.0-SNAPSHOT"
  val ScalaVersion = "2.10.4"

  lazy val main = Project("finder", file("."), settings = defaultSettings)
    .aggregate(core)

  lazy val core = Project("finder-core", file("core"), settings = defaultSettings)
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.2.1",
        "org.iq80.leveldb" % "leveldb" % "0.7",
        "com.github.tototoshi" % "scala-csv_2.10" % "1.1.2",
        "com.hadoop.gplcompression" % "hadoop-lzo" % "0.4.19",
        "org.apache.hadoop" % "hadoop-client" % "2.0.0-mr1-cdh4.2.1",
        "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.4",
        "org.scalatest" %% "scalatest" % "2.2.0" % "test"
      )
    )

  lazy val defaultSettings = Defaults.coreDefaultSettings ++ Seq(
    organization := "in.ashwanthkumar",
    version := AppVersion,
    resolvers += "Twitter Maven Repo" at "http://maven.twttr.com/",
    resolvers += "Cloudera Repo" at "https://repository.cloudera.com/cloudera/cloudera-repos/",
    parallelExecution in This := false
  )

}
