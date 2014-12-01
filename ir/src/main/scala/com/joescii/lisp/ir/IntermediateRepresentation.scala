package com.joescii.lisp.ir

import com.joescii.lisp.parser.ast._

object IntermediateRepresentation {
  def interpret(program:ProgramNode):Program = {
    val values = program.lisps.flatMap { l =>
      l.atoms.headOption match {
        case Some(SymbolNode("print")) =>
          toValues(l.atoms.tail).map(Print(_))
        case _ => List()
      }
    }

    Program(values)
  }

  private def toValues(nodes:List[AstNode]) = nodes.map {
    case StringNode(str) => StringVal(str)
  }
  private def toValue(node:StringNode) = StringVal(node.string)
}
