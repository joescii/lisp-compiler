package com.joescii.lisp

import jvmc.HelloWorld
import parser.LispParser

import java.io.{FileOutputStream, File}

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
    val code = """(print "Hello World!")"""
    println("Compiling: "+code)
    val program = LispParser.parseWithTree(code)
    println(program)

    val jite = HelloWorld.jite

    val helloClassFile = new File(target, "HelloJite.class")

    val outStream = new FileOutputStream(helloClassFile)
    outStream.write(jite.toBytes)
    outStream.flush()
    outStream.close()

    Seq(helloClassFile)
  }
}
