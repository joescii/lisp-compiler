package com.joescii.lisp
package metal
package js

import ir._
import java.io.{FileOutputStream, PrintStream, File}

import net.liftweb.json.JsonAST._
import net.liftweb.http.js._
import JE._
import JsCmds._

object JsMetal extends Metal {
  def toJs(program:Program):JsFileContents = {
    val jsCmds = program.vs.map {
      case Print(s) => print(s)
      case _ => Noop
    }

    jsCmds.map(_.toJsCmd).mkString("\n")
  }

  override def forge(program:Program, target:File):Seq[File] = {
    target.mkdirs()
    val outFile = new File(target, "HelloJs.js")
    val out = new PrintStream(new FileOutputStream(outFile))
    val js = toJs(program)

//    out.println("#!/usr/bin/jjs")
    out.println(js)
    out.flush()
    out.close()

    Seq(outFile)
  }

  def print(v:Value):JsCmd = v match {
    case StringVal(s) => Call("print", JString(s))
    case _ => Noop
  }

}
