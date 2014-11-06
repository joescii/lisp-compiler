import sbt._
import Keys._

object CompilerBuild extends Build {

  val commonSettings = Seq(
    organization := "com.joescii",
    scalaVersion := "2.10.4",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    version := "0.0.1",
    resolvers ++= Seq(
      "sonatype-public" at "https://oss.sonatype.org/content/groups/public"
    )
  )

  lazy val parser = Project(
    id = "parser",
    base = file("parser"),
    settings = commonSettings ++ Seq(
      name := "parser",
      description := "Source parser",
      libraryDependencies ++= Seq(
        "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"
      )
    )
  )

  lazy val jvm_target = Project(
    id = "jvm-target",
    base = file("jvm-target"),
    settings = commonSettings ++ Seq(
      name := "jvm-target",
      description := "JVM bytecode target compiler",
      libraryDependencies ++= Seq(
      )
    )
  ).dependsOn(parser)

  lazy val js_target = Project(
    id = "js-target",
    base = file("js-target"),
    settings = commonSettings ++ Seq(
      name := "js-target",
      description := "JavaScript target compiler",
      libraryDependencies ++= Seq(
      )
    )
  ).dependsOn(parser)
  
  lazy val compiler = Project(
    id = "compiler",
    base = file("compiler"),
    settings = commonSettings ++ Seq(
      name := "compiler",
      description := "All of this crap rolled into a compiler",
      libraryDependencies ++= Seq(
      )
    )
  ).dependsOn(parser, jvm_target, js_target)
  
  lazy val plugin = Project(
    id = "plugin",
    base = file("plugin"),
    settings = commonSettings ++ Seq(
      name := "plugin",
      description := "The compiler packaged as an sbt plugin",
      sbtPlugin := true,
      libraryDependencies ++= Seq(
      )
    )
  ).dependsOn(compiler)

  lazy val root = Project(
    id = "root",
    base = file(".")
  ).aggregate(parser, jvm_target, js_target, compiler, plugin)

}
