package com.joescii.lisp
package compiler

import metal.jvm.ClassFileContents
import metal.js.JsFileContents
import javax.script.ScriptEngineManager

object ProgramRunner {
  def run(program:String):(String, String) = {
    val (byteCodes, scripts) = Compiler.compile(Seq(program))
    val jvm = runJvm(byteCodes)
    val js  = runJs(scripts)
    (jvm, js)
  }

  def runJvm(byteCodes:Seq[ClassFileContents]):String = {
    ""
  }

  def runJs(scripts:Seq[JsFileContents]):String = {
    val engineManager = new ScriptEngineManager(null)
    val engine = engineManager.getEngineByName("nashorn")
    engine.eval(scripts.head) // Prints to console
    ""
  }
}
