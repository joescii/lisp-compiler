package com.joescii.lisp
package parser

import ast._

import org.parboiled.scala._
import org.parboiled.errors.{ParsingException, ErrorUtils}
import org.parboiled.support.{DebuggingValueStack, ParseTreeUtils}

object LispParser {
  def parse(program:String):ProgramNode = {
    val result = new LispParser().parse(program)
    result.valueStack.pop().asInstanceOf[ProgramNode]
  }

  def parseWithTree(program:String):ProgramNode = {
    val result = new LispParser().parse(program)
    val tree = ParseTreeUtils.printNodeTree(result)
    println(tree)
    result.valueStack.pop().asInstanceOf[ProgramNode]
  }
}

class LispParser extends Parser {
  override val buildParseTree = true

  def Prog = rule { zeroOrMore(WhiteSpace) ~ zeroOrMore(Lst, separator = zeroOrMore(WhiteSpace)) ~~> ProgramNode }

  def Lst:Rule1[ListNode] = rule { "(" ~ oneOrMore(Atom | Lst, separator = WhiteSpace) ~ ")" ~~> ListNode }

  def Atom:Rule1[AtomNode] = rule ( Symbol | Number | Strng )

  def Symbol = rule { oneOrMore(AlphaChar | SpecialChar) ~> SymbolNode ~ zeroOrMore(AlphaChar | Digit | SpecialChar) }

  def SpecialChar = rule { anyOf("~!@#%^&*-+=:<>?/\\|") }

  def AlphaChar = rule { "a" - "z" | "A" - "Z" | "_" }

  def Strng = rule { "\"" ~ zeroOrMore(Character) ~> StringNode ~ "\"" }

  def Character = rule { EscapedChar | NormalChar }

  def EscapedChar = rule { "\\" ~ (anyOf("\"\\/bfnrt") | Unicode) }

  def NormalChar = rule { !anyOf("\"\\") ~ ANY }

  def Unicode = rule { "u" ~ HexDigit ~ HexDigit ~ HexDigit ~ HexDigit }

  def HexDigit = rule { "0" - "9" | "a" - "f" | "A" - "Z" }

  def Number = rule { optional("-") ~ (("1" - "9") ~ Digits | Digit) ~> (text => NumberNode(text.toInt)) }

  def Digits = rule { oneOrMore(Digit) }

  def Digit = rule { "0" - "9" }

  def WhiteSpace = rule { anyOf(" \n\r\t\f") }

  def parse(program: String) = {
    ReportingParseRunner(Prog).run(program)
  }
}
