package uk.co.kenfos.crawler.service

import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest._

class CrawlerIntegrationSpec extends AsyncWordSpec with Matchers with BeforeAndAfterEach with AsyncMockFactory {
  "Crawler" should {
    "make HTTP request and call scraper" in {
      val domain = "http://www.kenfos.co.uk"
      val url = "http://www.kenfos.co.uk"
      val links = Set("http://www.kenfos.co.uk/about", "http://www.kenfos.co.uk/author/davidmolinero/")
      val expectedLinks = Set("http://www.kenfos.co.uk/about", "http://www.kenfos.co.uk/author/davidmolinero/")
      val scraper = mock[Scraper]
      val crawler = new HTTPCrawler(scraper)
      scraper.getLinks _ expects * returning links
      crawler.crawl(domain, url).map(response => response shouldBe expectedLinks)
    }

    "should include root domain in relative links" in {
      val domain = "http://www.kenfos.co.uk"
      val url = "http://www.kenfos.co.uk"
      val links = Set("http://www.kenfos.co.uk/about", "/author/davidmolinero/")
      val expectedLinks = Set("http://www.kenfos.co.uk/about", "http://www.kenfos.co.uk/author/davidmolinero/")
      val scraper = mock[Scraper]
      val crawler = new HTTPCrawler(scraper)
      scraper.getLinks _ expects * returning links
      crawler.crawl(domain, url).map(response => response shouldBe expectedLinks)
    }

    "filter out non web links" in {
      val domain = "http://www.kenfos.co.uk"
      val url = "http://www.kenfos.co.uk"
      val links = Set("http://www.kenfos.co.uk/about", "mailto:davidmolinero.com@gmail.com")
      val expectedLinks = Set("http://www.kenfos.co.uk/about")
      val scraper = mock[Scraper]
      val crawler = new HTTPCrawler(scraper)
      scraper.getLinks _ expects * returning links
      crawler.crawl(domain, url).map(response => response shouldBe expectedLinks)
    }
  }
}
