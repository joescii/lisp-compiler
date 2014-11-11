package com.joescii.lisp

import parser.LispParser
import jvm.metal.JvmMetal

import java.io.{FileOutputStream, File}
import scala.io.Source

object CompilerMain extends App {
  if(args.length < 3){
    println("Usage: lispc <src> <jvmTarget> <jsTarget>")
    System exit 1
  }

  val srcName = args(0)
  val jvmTargetName = args(1)
  val jsTargetName = args(2)

  val src = new File(srcName)
  val jvmTarget = new File(jvmTargetName)
  val jsTarget = new File(jsTargetName)

  if(!jvmTarget .isDirectory) {
    println("<jvmTarget > must be a writable directory")
    System exit 1
  }
  if(!jsTarget .isDirectory) {
    println("<jsTarget> must be a writable directory")
    System exit 1
  }

  Compiler.compile(src, jvmTarget, jsTarget)
}

object Compiler {
  def compile(src:File, jvmTarget:File, jsTarget:File):(Seq[File], Seq[File]) = {
    val srcs = if(src.isDirectory) {
      src.listFiles().filter(_.getName.endsWith(".lisp")).toList
    } else if(src.getName.endsWith(".lisp")) {
      List(src)
    } else {
      List()
    }

    println("Compiling "+srcs.length+" sources")

    val programs = srcs.map { s =>
      val code = Source.fromFile(s, "utf-8").mkString
      LispParser.parse(code)
    }

    val classFiles = programs.flatMap(p => JvmMetal.forge(p, jvmTarget))

    (classFiles, Seq())
  }
}
