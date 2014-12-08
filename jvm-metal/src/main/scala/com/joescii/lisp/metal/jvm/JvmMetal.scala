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
  type LocalVariables = Seq[String]

  private def loadValue(c:CodeBlock, locals:LocalVariables, v:Value) = v match {
    case StringVal(s) => c.ldc(s)
    //        case IntVal(i) => i.toString
    case SymbolicName(name) => c.aload(locals.indexOf(name))
  }

  def print(c:CodeBlock, locals:LocalVariables, v:Value) = {
    c.getstatic(p(classOf[System]), "out", ci(classOf[PrintStream]))
    loadValue(c, locals, v)
      .invokevirtual(p(classOf[PrintStream]), "println", sig(classOf[Unit], classOf[String]))
  }
  def declareValue(c:CodeBlock, locals:LocalVariables, name:String, v:Value) = {
    v match {
      case StringVal(s) =>
        c.ldc(s)
          .astore(locals.length)
    }
  }

  def apply(program:Program) = new JiteClass("HelloJite") {
    val locals = Seq("args")
    val codeAndLocals = program.vs.foldLeft((new CodeBlock(), locals)) {
      case ((c, vs), Print(v)) => (print(c, vs, v), vs)
      case ((c, vs), Let(SymbolicName(name), v)) => (declareValue(c, vs, name, v), vs :+ name)
    }

    defineMethod("main", ACC_PUBLIC | ACC_STATIC, sig(classOf[Unit], classOf[Array[String]]), codeAndLocals._1.voidreturn())
  }


}