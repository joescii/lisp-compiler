package com.joescii.lisp
package parser

import ast._

import org.parboiled.scala._
import org.parboiled.errors.{ParsingException, ErrorUtils}
import org.parboiled.support.{DebuggingValueStack, ParseTreeUtils}

class LispParser extends Parser {
  override val buildParseTree = true

  def Prog = rule { zeroOrMore(Lst, separator = WhiteSpace) ~~> Program }

  def Lst = rule { "(" ~ oneOrMore(Atom, separator = WhiteSpace) ~ ")" ~~> ListNode }

  def Atom:Rule1[AtomNode] = rule ( Symbol | Number | Strng )

  def Symbol = rule { oneOrMore(AlphaChar) ~> SymbolNode ~ zeroOrMore(AlphaChar | Digit) }

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

  def calculate(expression: String) = {
    val parsingResult = ReportingParseRunner(Prog).run(expression)
    val tree = ParseTreeUtils.printNodeTree(parsingResult)
    println(tree)
    println(parsingResult.valueStack.peek)
  }
}
