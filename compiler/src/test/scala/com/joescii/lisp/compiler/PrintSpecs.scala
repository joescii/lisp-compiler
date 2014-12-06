package com.joescii.lisp.compiler

import org.scalatest.{ShouldMatchers, WordSpec}
import ProgramRunner.{ run => r }

class PrintSpecs extends WordSpec with ShouldMatchers {
  "The Print function" should {
    "print 'Roll Tide'" in {
      val prog =
        """(print "Roll Tide")"""
      val (jvm, js) = r(prog)
      js should be (Seq("Roll Tide"))
      jvm should be(js)
    }

    "print 'Roll', 'Tide', 'Roll' on three separate lines" in {
      val prog =
        """
          (print "Roll")
          (print "Tide")
          (print "Roll")
        """
      val (jvm, js) = r(prog)
      js should be (Seq("Roll", "Tide", "Roll"))
      jvm should be(js)
    }

  }
}
