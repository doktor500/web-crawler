package uk.co.kenfos.crawler.actorsystem.domain

import org.scalatest.{Matchers, WordSpec}
import uk.co.kenfos.crawler.domain.Url

class SiteGraphSpec extends WordSpec with Matchers {

  val url = Url("http://kenfos.co.uk")
  val aboutUrl = Url("http://kenfos.co.uk/about")
  val nonExistingUrl = Url("http://davidmolinero.com")

  "SiteGraph" should {
    "return true when url is the same as the root domain" in {
      val siteGraph = SiteGraph(url)
      siteGraph.contains(url) shouldBe true
      siteGraph.contains(nonExistingUrl) shouldBe false
    }

    "return true when url exists in as a referenced link in the url graph" in {
      val siteGraph = SiteGraph(url, Map(aboutUrl -> Set()))
      siteGraph.contains(aboutUrl) shouldBe true
      siteGraph.contains(nonExistingUrl) shouldBe false
    }

    "return true when url exists as a reference in the url graph" in {
      val siteGraph = SiteGraph(url, Map(url -> Set(aboutUrl)))
      siteGraph.contains(aboutUrl) shouldBe true
      siteGraph.contains(nonExistingUrl) shouldBe false
    }

    "return a new site graph when a new url is added" in {
      val siteGraph = SiteGraph(url)
      val newSiteGraph = siteGraph.add(aboutUrl, Set())
      newSiteGraph shouldBe SiteGraph(url, Map(aboutUrl -> Set()))
    }
  }
}
