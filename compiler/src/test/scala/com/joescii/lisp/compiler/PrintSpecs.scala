package com.joescii.lisp.compiler

import org.scalatest.{ShouldMatchers, WordSpec}
import ProgramRunner.{ run => r }

class PrintSpecs extends WordSpec with ShouldMatchers {
  "The Print command" should {
    "work or whatever" in {
      val prog =
        """(print "Roll Tide")"""
      val (jvm, js) = r(prog)
      js should be (List("Roll Tide"))
    }
  }
}
