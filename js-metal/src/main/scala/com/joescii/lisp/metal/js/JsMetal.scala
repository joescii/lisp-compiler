package com.joescii.lisp
package metal
package js

import parser.ast._
import java.io.{FileOutputStream, PrintStream, File}

object JsMetal extends Metal {
  override def forge(program:Program, target:File):Seq[File] = {
    val outFile = new File(target, "HelloJs.js")
    val out = new PrintStream(new FileOutputStream(outFile))

    out.println("""console.log('Hello world!');""")
    out.flush()
    out.close()

    Seq(outFile)
  }
}
