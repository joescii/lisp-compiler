package com.joescii.lisp
package plugin

import com.joescii.lisp. { Compiler => LispC }

import sbt._
import Keys._

object LispPlugin extends Plugin {
  val lispcKey = TaskKey[Seq[File]]("lispc", "Compiles lisp")

  val lispcImpl = lispcKey <<=
    (sourceDirectory in Compile, classDirectory in Compile) map { (src, dst) =>
      val lisp = src / "lisp"
      dst.mkdirs()
      LispC.compile(lisp, dst)
    }

  val lispcSettings: Seq[Def.Setting[_]] = Seq(
    lispcImpl,
    (compile in Compile) <<= (compile in Compile) dependsOn lispcKey
    comp
  )

}
