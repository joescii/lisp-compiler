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
  def toJs(program:Program):List[String] = {
    val jsCmds = program.vs.map {
      case Print(s) => print(s)
      case _ => Noop
    }

    jsCmds.map(_.toJsCmd)
  }

  override def forge(program:Program, target:File):Seq[File] = {
    val outFile = new File(target, "HelloJs.js")
    val out = new PrintStream(new FileOutputStream(outFile))
    val js = toJs(program)

    out.println("#!/usr/bin/jjs")
    js.foreach(out.print)
    out.flush()
    out.close()

    Seq(outFile)
  }

  def print(v:Value):JsCmd = v match {
    case StringVal(s) => Call("print", JString(s))
    case _ => Noop
  }

}
