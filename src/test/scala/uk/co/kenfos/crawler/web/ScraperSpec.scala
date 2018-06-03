package uk.co.kenfos.crawler.web

import org.scalatest.{AsyncWordSpec, Matchers}

class ScraperSpec extends AsyncWordSpec with Matchers {
  "Scraper" should {
    "return the links in an HTML document" in {
      val link1 = "http://kenfos.co.uk"
      val link2 = "http://kenfos.co.uk/about"
      HTMLScraper.getLinks(
        s"""<html>
           |<body>
           |<a href="$link1"></a>
           |<a href="$link2"></a>
           |</body>
           |</html>""".stripMargin
      ).map(response => response shouldBe Set(link1, link2))
    }
  }
}
