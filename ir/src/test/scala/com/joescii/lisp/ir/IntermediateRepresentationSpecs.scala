package com.joescii.lisp.ir

import org.scalatest.{ShouldMatchers, WordSpec}
import com.joescii.lisp.parser.ast._

/**
 * Created by jbarnes on 11/30/2014.
 */
class IntermediateRepresentationSpecs extends WordSpec with ShouldMatchers {
  "The Intermediate Representation" should {
    "detect a print" in {
      val prog = Program(List(
        ListNode(List(
          SymbolNode("print"),
          StringNode("Roll Tide!")
        ))
      ))
    }
  }
}
