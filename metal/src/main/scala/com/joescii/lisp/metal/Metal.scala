package com.joescii.lisp
package metal

import parser.ast._

/**
 * Base trait for METAL!  (i.e. a compilation target)
 */
trait Metal {
  def compile(ast:Program):Array[Byte]
}
