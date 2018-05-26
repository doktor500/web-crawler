package uk.co.kenfos.crawler.domain

import org.scalatest.{Matchers, WordSpec}

class SiteGraphSpec extends WordSpec with Matchers {

  val url = "http://kenfos.co.uk"
  val aboutUrl = "http://kenfos.co.uk/about"
  val nonExistingUrl = "http://davidmolinero.com"

  "SiteGraph" should {
    "returns true when url is the same as the root domain" in {
      val siteGraph = SiteGraph(url)
      siteGraph.contains(url) shouldBe true
      siteGraph.contains(nonExistingUrl) shouldBe false
    }

    "returns true when url exists in a root node of the url graph" in {
      val siteGraph = SiteGraph(url, Map(Node(aboutUrl) -> Set()))
      siteGraph.contains(aboutUrl) shouldBe true
      siteGraph.contains(nonExistingUrl) shouldBe false
    }

    "returns true when url exists as a reference in the url graph" in {
      val siteGraph = SiteGraph(url, Map(Node(url) -> Set(Node(aboutUrl))))
      siteGraph.contains(aboutUrl) shouldBe true
      siteGraph.contains(nonExistingUrl) shouldBe false
    }

    "returns a new site graph when a new node is added" in {
      val siteGraph = SiteGraph(url)
      val newSiteGraph = siteGraph.add(aboutUrl, Set())
      newSiteGraph shouldBe SiteGraph(url, Map(Node(aboutUrl) -> Set()))
    }
  }
}
