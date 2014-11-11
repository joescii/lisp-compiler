package com.joescii.lisp
package metal
package jvm

import parser.ast._
import java.io.{FileOutputStream, File}

/**
 * Created by jbarnes on 11/10/2014.
 */
object JvmMetal {
  def forge(program:Program, target:File):Seq[File] = {
    val jite = HelloWorld.jite

    val helloClassFile = new File(target, "HelloJite.class")

    val outStream = new FileOutputStream(helloClassFile)
    outStream.write(jite.toBytes)
    outStream.flush()
    outStream.close()

    Seq(helloClassFile)
  }

}
