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
    val jvm = runJvm(classFiles)
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

  def runJvm(byteCodes:Seq[File]):Seq[String] = {
    Seq()
  }

  def runJs(scripts:Seq[File]):Seq[String] = {
    import scala.collection.JavaConverters._
    val cp = System.getProperty("java.class.path")
    val java = System.getProperty("java.home") + "/bin/java"
    val scriptNames = scripts.map(_.getCanonicalPath)
    val proc = new ProcessBuilder((List(java, "-classpath", cp, "com.joescii.lisp.compiler.JsRunner") ++ scriptNames).asJava).start()
    val buffer = Source.fromInputStream(proc.getInputStream, "utf-8")
    val output = buffer.getLines().toSeq
    proc.waitFor()
    output
  }
}

object JsRunner extends App {
  val engineManager = new ScriptEngineManager(null)
  val engine = engineManager.getEngineByName("nashorn")
  args.map(Source.fromFile(_, "utf-8"))
    .map(_.getLines().mkString("\n"))
    .foreach(engine.eval)
}