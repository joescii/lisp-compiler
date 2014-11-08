package com.joescii.lisp.jvmc

import me.qmx.jitescript._
import org.objectweb.asm.Opcodes._
import me.qmx.jitescript.util.CodegenUtils._
import java.io.PrintStream

object HelloWorld {
  lazy val jite = new JiteClass("HelloJite") {
    defineMethod("main", ACC_PUBLIC | ACC_STATIC, sig(classOf[Unit], classOf[Array[String]]), new CodeBlock()
      .ldc("HelloWorld!")
      .getstatic(p(classOf[System]), "out", ci(classOf[PrintStream]))
      .swap()
      .invokevirtual(p(classOf[PrintStream]), "println", sig(classOf[Unit], classOf[Object]))
      .voidreturn()
    )
  }
}
