package com.joescii.lisp.ir

import org.scalatest.{ShouldMatchers, WordSpec}
import com.joescii.lisp.parser.ast._

/**
 * Created by jbarnes on 11/30/2014.
 */
class IntermediateRepresentationSpecs extends WordSpec with ShouldMatchers {
  "The Intermediate Representation" should {
    "detect a print" in {
      val progAst = ProgramNode(List(
        ListNode(List(
          SymbolNode("print"),
          StringNode("Roll Tide!")
        ))
      ))
      val prog = Program(List(
        Print(StringVal("Roll Tide!"))
      ))

      IntermediateRepresentation.interpret(progAst) should be (prog)
    }
  }
}
