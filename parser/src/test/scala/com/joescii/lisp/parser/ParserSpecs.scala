package com.joescii.lisp.parser

import ast._
import org.scalatest._

class ParserSpecs extends WordSpec with ShouldMatchers {
  "The Parser" should {
    "parse a single line with one symbol" in {
      val code = "(symbol)"
      val program = LispParser.parse(code)

      program should be (
        Program(List(
          ListNode(List(
            SymbolNode("symbol")
          ))
        ))
      )
    }

    "parse a single line with one number" in {
      val code = "(42)"
      val program = LispParser.parse(code)

      program should be (
        Program(List(
          ListNode(List(
            NumberNode(42)
          ))
        ))
      )
    }

    "parse a single line with one string" in {
      val code = """("Roll Tide!")"""
      val program = LispParser.parse(code)

      program should be (
        Program(List(
          ListNode(List(
            StringNode("Roll Tide!")
          ))
        ))
      )
    }

    "parse a single line with a symbol, a string, and a number" in {
      val code = """(concat "Roll Tide!" 16)"""
      val program = LispParser.parse(code)

      program should be (
        Program(List(
          ListNode(List(
            SymbolNode("concat"),
            StringNode("Roll Tide!"),
            NumberNode(16)
          ))
        ))
      )
    }

    "parse a single line with three symbols, including a special symbol" in {
      val code = "(+ a b)"
      val program = LispParser.parse(code)

      program should be(
        Program(List(
          ListNode(List(
            SymbolNode("+"),
            SymbolNode("a"),
            SymbolNode("b")
          ))
        ))
      )
    }

    "parse a single line with multi-char special-char symbol, several other args, and leading whitespace" in {
      val code =
        """
          (~~> a b c 15 "blah")
        """
      val program = LispParser.parse(code)

      program should be (
        Program(List(
          ListNode(List(
            SymbolNode("~~>"),
            SymbolNode("a"),
            SymbolNode("b"),
            SymbolNode("c"),
            NumberNode(15),
            StringNode("blah")
          ))
        ))
      )
    }

    "parse a multi-line program" in {
      val code =
        """
          (~~> a b c 15 "blah")
          (<> e eff gee)
        """
      val program = LispParser.parse(code)

      program should be (
        Program(List(
          ListNode(List(
            SymbolNode("~~>"),
            SymbolNode("a"),
            SymbolNode("b"),
            SymbolNode("c"),
            NumberNode(15),
            StringNode("blah")
          )),
          ListNode(List(
            SymbolNode("<>"),
            SymbolNode("e"),
            SymbolNode("eff"),
            SymbolNode("gee")
          ))
        ))
      )
    }

    "parse a list within a list" in {
      val code = "(+ 1 (* 4 5) 10)"
      val program = LispParser.parse(code)

      program should be (
        Program(List(
          ListNode(List(
            SymbolNode("+"),
            NumberNode(1),
            ListNode(List(
              SymbolNode("*"),
              NumberNode(4),
              NumberNode(5)
            )),
            NumberNode(10)
          ))
        ))
      )

    }
  }
}
