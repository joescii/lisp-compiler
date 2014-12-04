package com.joescii.lisp
package compiler

import metal.jvm.ClassFileContents
import metal.js.JsFileContents
import javax.script.ScriptEngineManager
import scala.io.Source

object ProgramRunner {
  def run(program:String):(List[String], List[String]) = {
    val (byteCodes, scripts) = Compiler.compile(Seq(program))
    val jvm = runJvm(byteCodes)
    val js  = runJs(scripts)
    (jvm, js)
  }

  def runJvm(byteCodes:Seq[ClassFileContents]):List[String] = {
    List()
  }

  def runJs(scripts:Seq[JsFileContents]):List[String] = {
    val cp = System.getProperty("java.class.path")
    val java = System.getProperty("java.home") + "/bin/java"
    val proc = new ProcessBuilder(java, "-classpath", cp, "com.joescii.lisp.compiler.JsRunner").start()
    val buffer = Source.fromInputStream(proc.getInputStream, "utf-8")
    val output = buffer.getLines().toList
    proc.waitFor()
    output
  }
}

object JsRunner extends App {
  val engineManager = new ScriptEngineManager(null)
  val engine = engineManager.getEngineByName("nashorn")
  args.map(Source.fromFile(_, "utf-8"))
    .map(_.getLines().mkString("\n"))
    .map(engine.eval)
  println("Roll Tide")
}