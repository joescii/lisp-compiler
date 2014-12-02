import sbt._
import Keys._

object CompilerBuild extends Build {

  val commonSettings = Seq(
    organization := "com.joescii",
    scalaVersion := "2.10.4",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    version := "0.0.1-SNAPSHOT",
    resolvers ++= Seq(
      "sonatype-public" at "https://oss.sonatype.org/content/groups/public"
    )
  )
  val parboiled = "org.parboiled" %% "parboiled-scala" % "1.1.6" % "compile"
  val scalatest = "org.scalatest" %% "scalatest"       % "2.2.1" % "test"
  val lift      = "net.liftweb"   %% "lift-webkit"     % "2.5.1" % "compile"

  lazy val parser = Project(
    id = "parser",
    base = file("parser"),
    settings = commonSettings ++ Seq(
      name := "parser",
      description := "Source parser",
      libraryDependencies ++= Seq(
        parboiled,
        scalatest
      )
    )
  )

  lazy val ir = Project(
    id = "intermediate-representation",
    base = file("ir"),
    settings = commonSettings ++ Seq(
      name := "ir",
      description := "Intermediate Representation",
      libraryDependencies ++= Seq(
        scalatest
      )
    )
  ).dependsOn(parser)

  lazy val metal = Project(
    id = "metal",
    base = file("metal"),
    settings = commonSettings ++ Seq(
      name := "metal",
      description := "Abstraction of metal to be compiled to",
      libraryDependencies ++= Seq(
      )
    )
  ).dependsOn(ir)

  lazy val jvm_metal = Project(
    id = "jvm-metal",
    base = file("jvm-metal"),
    settings = commonSettings ++ Seq(
      name := "jvm-metal",
      description := "Compiles to the JVM bytecode metal",
      libraryDependencies ++= Seq(
        "me.qmx.jitescript" % "jitescript" % "0.3.0" % "compile"
      )
    )
  ).dependsOn(metal, ir)

  lazy val js_metal = Project(
    id = "js-metal",
    base = file("js-metal"),
    settings = commonSettings ++ Seq(
      name := "js-metal",
      description := "Compiles to the JS metal",
      libraryDependencies ++= Seq(
        lift
      )
    )
  ).dependsOn(metal, ir)
  
  lazy val compiler = {
    import sbtassembly.Plugin._
    import AssemblyKeys._ 

    Project(
      id = "compiler",
      base = file("compiler"),
      settings = commonSettings ++ assemblySettings ++ Seq(
        name := "compiler",
        description := "All of this crap rolled into a compiler",
        jarName in assembly := "lispc.jar",
        mainClass in assembly := Some("com.joescii.lisp.CompilerMain"),
        fork in Test := true,
        libraryDependencies ++= Seq(
          scalatest
        )
      )
    ).dependsOn(parser, ir, jvm_metal, js_metal)
  }
  
  lazy val plugin = Project(
    id = "plugin",
    base = file("plugin"),
    settings = commonSettings ++ Seq(
      name := "lispc",
      description := "The compiler packaged as an sbt plugin",
      sbtPlugin := true,
      publishMavenStyle := false,
      libraryDependencies ++= Seq(
      )
    )
  ).dependsOn(compiler)

  lazy val root = Project(
    id = "lisp-compiler-projects",
    base = file(".")
  ).aggregate(parser, ir, metal, jvm_metal, js_metal, compiler, plugin)

}
