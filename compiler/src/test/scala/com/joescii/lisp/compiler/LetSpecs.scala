package com.joescii.lisp.compiler

import org.scalatest.{ShouldMatchers, WordSpec}
import ProgramRunner.{ run => r }

class LetSpecs extends WordSpec with ShouldMatchers {
  "The let function" should {
    "assigning a value to later be printed" in {
      val prog =
        """
          (let msg "Roll Tide")
          (print msg)
        """
      val (jvm, js) = r(prog)
      js should be(Seq("Roll Tide"))
//      jvm should be(js)
    }

  }
}
