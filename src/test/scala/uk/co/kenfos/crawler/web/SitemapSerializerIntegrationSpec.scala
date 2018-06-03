package uk.co.kenfos.crawler.web

import org.scalatest.{Matchers, WordSpec}
import uk.co.kenfos.crawler.domain.Url

import scala.io.Source

class SitemapSerializerIntegrationSpec extends WordSpec with Matchers {
  "Sitemap serializer" should {
    "create the graph JSON file" in {
      val siteGraph = Map(Url("https://www.thoughtworks.com") -> Set(Url("https://www.thoughtworks.com/about-us")))

      JsonSerializer.serialize(siteGraph)
      val content = Source.fromFile("output.json").getLines.mkString
      content shouldBe
        """{  "https://www.thoughtworks.com" : [ {    "value" : "https://www.thoughtworks.com/about-us"  } ]}"""
    }
  }
}
