package com.joescii.lisp
package metal
package js

import parser.ast._

object JsTarget extends Metal {
  override def compile(ast:Program) = {
    "".getBytes("utf-8")
  }
}
