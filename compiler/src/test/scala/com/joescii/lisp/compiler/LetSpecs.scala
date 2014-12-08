package com.joescii.lisp.compiler

import org.scalatest.{ShouldMatchers, WordSpec}
import ProgramRunner.{ run => r }

class LetSpecs extends WordSpec with ShouldMatchers {
  "The let function" should {
    "allow assigning a value to later be printed" in {
      val prog =
        """
          (let msg "Roll Tide")
          (print msg)
        """
      val (jvm, js) = r(prog)
      js should be(Seq("Roll Tide"))
      jvm should be(js)
    }

    "allow assigning two values to later be printed" in {
      val prog =
        """
          (let msg1 "Led Zeppelin")
          (let msg2 "Black Sabbath")
          (print msg2)
          (print msg1)
        """
      val (jvm, js) = r(prog)
      js should be(Seq("Black Sabbath", "Led Zeppelin"))
      jvm should be(js)
    }

  }
}
