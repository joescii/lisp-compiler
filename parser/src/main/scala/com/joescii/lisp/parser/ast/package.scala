package com.joescii.lisp.parser

package object ast {

  trait AstNode
  trait AtomNode extends AstNode
  case class SymbolNode(symbol:String)      extends AtomNode
  case class NumberNode(number:Int)         extends AtomNode
  case class StringNode(string:String)      extends AtomNode
  case class ListNode(atoms:List[AstNode])  extends AstNode

  case class Program(lisps:List[ListNode])
}
