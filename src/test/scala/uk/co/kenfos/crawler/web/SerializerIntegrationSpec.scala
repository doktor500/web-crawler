package uk.co.kenfos.crawler.web

import org.scalatest.{Matchers, WordSpec}
import uk.co.kenfos.crawler.domain.Url

import scala.io.Source

class SerializerIntegrationSpec extends WordSpec with Matchers {
  "Serializer" should {
    "create the graph JSON file" in {
      val siteGraph = Map(
        Url("http://www.kenfos.co.uk") -> Set(Url("http://www.kenfos.co.uk/about"))
      )

      JsonSerializer.serialize(siteGraph)
      val content = Source.fromFile("graph.json").getLines.mkString
      content shouldBe """{  "http://www.kenfos.co.uk" : [ {    "value" : "http://www.kenfos.co.uk/about"  } ]}"""
    }
  }
}
