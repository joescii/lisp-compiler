package com.joescii.lisp
package compiler

import javax.script.ScriptEngineManager
import scala.io.Source
import java.nio.file.Files
import java.io.{PrintStream, File}

object ProgramRunner {
  def run(program:String):(Seq[String], Seq[String]) = {
    val temp = Files.createTempDirectory("lispc-test").toFile
    val jsDir = new File(temp, "js")
    val classesDir = new File(temp, "classes")
    val srcsDir = new File(temp, "src")
    val src = new File(srcsDir, "test.lisp")

    srcsDir.mkdirs()
    write(program, src)

    val (classFiles, jsFiles) = Compiler.compile(src, classesDir, jsDir)
    val jvm = runJvm(classesDir)
    val js  = runJs(jsFiles)

    delete(temp)

    (jvm, js)
  }

  private def write(contents:String, dest:File) = {
    val printer = new PrintStream(dest)
    printer.print(contents)
  }

  private def delete(f:File):Unit = {
    if(f.isDirectory) f.listFiles() foreach delete
    f.delete()
  }

  def runJvm(classesDir:File):Seq[String] = {
    callJava(
      "HelloJite",
      cp = classesDir.getCanonicalPath
    )
  }

  def runJs(scripts:Seq[File]):Seq[String] = {
    val scriptNames = scripts.map(_.getCanonicalPath)
    callJjs(
      scripts = scriptNames
    )
  }

  private def callJava(clazz:String, cp:String) = {
    import scala.collection.JavaConverters._
    val java = System.getProperty("java.home") + "/bin/java"
    val builder = new ProcessBuilder(List(java, "-classpath", cp, clazz).asJava)
    toOutput(builder)
  }

  private def callJjs(scripts:Seq[String]) = {
    import scala.collection.JavaConverters._
    val jjs = System.getProperty("java.home") + "/bin/jjs"
    val builder = new ProcessBuilder((jjs +: scripts).asJava)
    toOutput(builder)
  }

  private def toOutput(builder:ProcessBuilder) = {
    builder.redirectErrorStream(true)
    val proc = builder.start()
    val buffer = Source.fromInputStream(proc.getInputStream, "utf-8")
    val output = buffer.getLines().toSeq
    proc.waitFor()
    output
  }

}
