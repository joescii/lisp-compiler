package com.joescii.lisp
package metal
package jvm

import ir._
import java.io.{PrintStream, FileOutputStream, File}
import me.qmx.jitescript.{CodeBlock, JiteClass}
import org.objectweb.asm.Opcodes._
import com.joescii.lisp.ir.Program
import me.qmx.jitescript.util.CodegenUtils._
import com.joescii.lisp.ir.Program

/**
 * Created by jbarnes on 11/10/2014.
 */
object JvmMetal extends Metal {
  override def forge(program:Program, target:File):Seq[File] = {
    val jite = LispJite(program)

    target.mkdirs()
    val helloClassFile = new File(target, "HelloJite.class")

    val outStream = new FileOutputStream(helloClassFile)
    outStream.write(jite.toBytes)
    outStream.flush()
    outStream.close()

    Seq(helloClassFile)
  }

}

object LispJite {
  implicit class LispCodeBlock(c:CodeBlock) {
    def print(v:Value) = {
      val str = v match {
        case StringVal(s) => s
        case IntVal(i) => i.toString
        case SymbolicName(name) => name
      }
      c.getstatic(p(classOf[System]), "out", ci(classOf[PrintStream]))
        .ldc(str)
        .invokevirtual(p(classOf[PrintStream]), "println", sig(classOf[Unit], classOf[String]))
    }
  }

  def apply(program:Program) = new JiteClass("HelloJite") {
    val code = program.vs.foldRight(new CodeBlock()) {
      case (Print(v), c) => c.print(v)
      case (Let(SymbolicName(name), v), c) => c
    }

    defineMethod("main", ACC_PUBLIC | ACC_STATIC, sig(classOf[Unit], classOf[Array[String]]), code.voidreturn())
  }


}