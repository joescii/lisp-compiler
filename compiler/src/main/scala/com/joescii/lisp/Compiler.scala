package com.joescii.lisp

import parser.LispParser
import jvm.metal.HelloWorld

import java.io.{FileOutputStream, File}
import scala.io.Source

object CompilerMain extends App {
  if(args.length < 2){
    println("Usage: lispc <src> <target>")
    System exit 1
  }

  val srcName = args(0)
  val targetName = args(1)

  val src = new File(srcName)
  val target = new File(targetName)

  if(!target.isDirectory) {
    println("<target> must be a writable directory")
    System exit 1
  }

  Compiler.compile(src, target)
}

object Compiler {
  def compile(src:File, target:File) = {
    val srcs = if(src.isDirectory) {
      src.listFiles().filter(_.getName.endsWith(".lisp")).toList
    } else if(src.getName.endsWith(".lisp")) {
      List(src)
    } else {
      List()
    }

    println("Compiling "+srcs.length+" sources")

    for {
      s <- srcs
    } {
      val code = Source.fromFile(s, "utf-8").mkString
      println("Compiling: "+code)
      val program = LispParser.parse(code)
      println(program)
    }

    val jite = HelloWorld.jite

    val helloClassFile = new File(target, "HelloJite.class")

    val outStream = new FileOutputStream(helloClassFile)
    outStream.write(jite.toBytes)
    outStream.flush()
    outStream.close()

    Seq(helloClassFile)
  }
}
