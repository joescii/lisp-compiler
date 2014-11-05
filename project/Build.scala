import sbt._
import Keys._

object CompilerBuild extends Build {

  val commonSettings = Seq(
    organization := "com.joescii",
    scalaVersion := "2.10.4",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    resolvers ++= Seq(
      "sonatype-public" at "https://oss.sonatype.org/content/groups/public"
    )
  )

  lazy val parser = Project(
    id = "parser",
    base = file("parser"),
    settings = commonSettings ++ Seq(
      name := "parser",
      version := "0.0.1",
      description := "Source parser",
      libraryDependencies ++= Seq(
        "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"
      )
    )
  )

  lazy val root = Project(
    id = "root",
    base = file(".")
  ).aggregate(parser)

}
