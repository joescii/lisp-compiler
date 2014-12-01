package com.joescii.lisp
package metal

import parser.ast._
import java.io.File

/**
 * Base trait for METAL!  (i.e. a compilation target)
 */
trait Metal {
  def forge(program:ProgramNode, target:File):Seq[File]
}
