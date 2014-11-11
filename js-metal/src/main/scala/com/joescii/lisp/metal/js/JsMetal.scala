package com.joescii.lisp
package metal
package js

import parser.ast._
import java.io.{FileOutputStream, PrintStream, File}

import net.liftweb.json.JsonAST._
import net.liftweb.http.js._
import JE._
import JsCmds._

object JsMetal extends Metal {
  override def forge(program:Program, target:File):Seq[File] = {
    val outFile = new File(target, "HelloJs.js")
    val out = new PrintStream(new FileOutputStream(outFile))

    val js = program.lisps.map { l =>
      l.atoms.headOption match {
        case Some(SymbolNode("print")) => print(l.atoms.tail)
        case _ => Noop
      }
    }

    js.foreach(j => out.print(j.toJsCmd))
    out.flush()
    out.close()

    Seq(outFile)
  }

  def print(atoms:List[AstNode]):JsCmd = {
    val printMe = atoms.headOption.map( _ match {
      case StringNode(str) => str
      case NumberNode(num) => num.toString
      case other => other.toString
    })

    printMe match {
      case Some(str) => Call("console.log", JString(str))
      case None => Call("console.log")
    }
  }
}
