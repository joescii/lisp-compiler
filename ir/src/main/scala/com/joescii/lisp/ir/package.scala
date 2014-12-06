package com.joescii.lisp

package object ir {
  case class Program(vs:List[Value])

  sealed trait Value
  case class StringVal(v:String) extends Value
  case class IntVal(i:Int) extends Value

  case class SymbolicName(name:String) extends Value

  case class Print(vs:Value) extends Value
  case class Let(name:SymbolicName, v:Value) extends Value
}
