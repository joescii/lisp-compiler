package com.joescii.lisp

import parser.LispParser
import com.joescii.lisp.metal.jvm.{ClassFileContents, JvmMetal}
import com.joescii.lisp.metal.js.{JsFileContents, JsMetal}

import java.io.File
import scala.io.Source
import com.joescii.lisp.ir.IntermediateRepresentation

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
    println("<jvmTarget> must be a writable directory")
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
      Seq(src)
    } else {
      Seq()
    }

    println("Compiling "+srcs.length+" sources")

    val programContents = srcs.map(Source.fromFile(_, "utf-8").mkString)
    val programTokens = programContents.map(LispParser.parse)
    val programs      = programTokens.map(IntermediateRepresentation.interpret)
    val classFiles    = programs.flatMap(p => JvmMetal.forge(p, jvmTarget))
    val jsFiles       = programs.flatMap(p => JsMetal.forge(p, jsTarget))

    (classFiles, jsFiles)
  }
}
