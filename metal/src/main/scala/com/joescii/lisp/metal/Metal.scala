package com.joescii.lisp
package metal

import ir._
import java.io.File

/**
 * Base trait for METAL!  (i.e. a compilation target)
 */
trait Metal {
  def forge(program:Program, target:File):Seq[File]
}
