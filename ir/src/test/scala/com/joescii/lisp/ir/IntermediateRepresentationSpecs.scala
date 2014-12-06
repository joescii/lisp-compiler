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

    "create multiple prints for a vararg-print" in {
      val progAst = ProgramNode(List(
        ListNode(List(
          SymbolNode("print"),
          StringNode("Roll"),
          StringNode("Tide"),
          StringNode("Roll")
        ))
      ))
      val prog = Program(List(
        Print(StringVal("Roll")),
        Print(StringVal("Tide")),
        Print(StringVal("Roll"))
      ))

      IntermediateRepresentation.interpret(progAst) should be (prog)
    }

    "handle let for assigning a string" in {
      val progAst = ProgramNode(List(
        ListNode(List(
          SymbolNode("let"),
          SymbolNode("name"),
          StringNode("NAME")
        ))
      ))
      val prog = Program(List(
        Let(SymbolicName("name"), StringVal("NAME"))
      ))

      IntermediateRepresentation.interpret(progAst) should be (prog)
    }
  }
}
