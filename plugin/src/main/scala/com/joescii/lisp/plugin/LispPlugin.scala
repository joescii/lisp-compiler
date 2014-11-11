package com.joescii.lisp
package plugin

import com.joescii.lisp. { Compiler => LispC }

import sbt._
import Keys._

object LispPlugin extends Plugin {
  val lispcKey = TaskKey[Seq[File]]("lispc", "Compiles lisp")

  val lispcImpl = lispcKey <<=
    (sourceDirectory in Compile, classDirectory in Compile, resourceManaged in Compile) map { (src, dst, rsrc) =>
      val lisp = src / "lisp"
      val lispjs = rsrc / "lispjs"
      dst.mkdirs()
      lispjs.mkdirs()
      val outFiles = LispC.compile(lisp, dst, lispjs)
      outFiles._1 ++ outFiles._2
    }

  val lispcSettings: Seq[Def.Setting[_]] = Seq(
    lispcImpl,
    (compile in Compile) <<= (compile in Compile) dependsOn lispcKey
  )

}
