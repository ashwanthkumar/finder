import sbt.Keys._
import sbt._

object Build extends Build {

  val AppVersion = "0.0.1"
  val ScalaVersion = "2.10.4"

  lazy val main = Project("finder", file("."), settings = defaultSettings)
    .aggregate(core, services)

  lazy val core = Project("finder-core", file("core"), settings = defaultSettings ++ publishSettings)
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.2.1",
        "org.iq80.leveldb" % "leveldb" % "0.7",
        "com.github.tototoshi" % "scala-csv_2.10" % "1.1.2",
        "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.4",
        "com.hadoop.gplcompression" % "hadoop-lzo" % "0.4.19" % "provided",
        "org.apache.hadoop" % "hadoop-client" % "2.0.0-mr1-cdh4.2.1" % "provided",
        "org.scalatest" %% "scalatest" % "2.2.0" % "test"
      )
    )

  lazy val services = Project("finder-services", file("services"), settings = defaultSettings)
    .settings(
      libraryDependencies ++= Seq(
        "com.twitter" %% "finatra" % "1.6.0"
      )
    )
    .dependsOn(core)

  lazy val defaultSettings = Defaults.coreDefaultSettings ++ Seq(
    organization := "in.ashwanthkumar",
    version := AppVersion,
    resolvers += "Twitter Maven Repo" at "http://maven.twttr.com/",
    resolvers += "Cloudera Repo" at "https://repository.cloudera.com/cloudera/cloudera-repos/",
    parallelExecution in This := false
  )

  lazy val publishSettings = Seq(
    mappings in (Compile, packageBin) ~= (_.filterNot{case (file, _) => file.isDirectory && file.getName == "my-config"}),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishArtifact in(Compile, packageDoc) := true,
    publishArtifact in(Compile, packageSrc) := true,
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := _pomExtra
  )

  val _pomExtra =
    <url>http://github.com/ashwanthkumar/finder</url>
      <licenses>
        <license>
          <name>Apache License, Version 2.0</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:ashwanthkumar/finder.git</url>
        <connection>scm:git:git@github.com:ashwanthkumar/finder.git</connection>
      </scm>
      <developers>
        <developer>
          <id>ashwanthkumar</id>
          <name>Ashwanth Kumar</name>
          <url>http://www.ashwanthkumar.in/</url>
        </developer>
      </developers>

}
