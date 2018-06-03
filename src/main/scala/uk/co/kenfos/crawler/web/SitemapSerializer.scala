package uk.co.kenfos.crawler.web

import java.io.{File, PrintWriter}

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

trait Serializer {
  def serialize(content: Any): Unit
}

object JsonSerializer extends Serializer {

  private val fileName = "output.json"

  def serialize(content: Any): Unit = {
    val objectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    writeToFile(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(content))
  }

  private def writeToFile(text: String): Unit = {
    val printerWriter = new PrintWriter(new File(fileName))
    printerWriter.write(text)
    printerWriter.close()
  }
}
