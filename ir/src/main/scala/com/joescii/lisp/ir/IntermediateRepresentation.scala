package com.joescii.lisp.ir

import com.joescii.lisp.parser.ast._

object IntermediateRepresentation {
  def interpret(program:ProgramNode):Program = {
    val values = program.lisps.flatMap { l =>
      l.atoms match {
        case SymbolNode("print") :: tail =>
          toValues(tail).map(Print(_))
        case SymbolNode("let") :: SymbolNode(name) :: StringNode(str) :: Nil =>
          List(Let(SymbolicName(name), StringVal(str)))
        case _ => List()
      }
    }

    Program(values)
  }

  private def toValues(nodes:List[AstNode]) = nodes.map {
    case StringNode(str) => StringVal(str)
    case SymbolNode(sym) => SymbolicName(sym)
  }
  private def toValue(node:StringNode) = StringVal(node.string)
}
