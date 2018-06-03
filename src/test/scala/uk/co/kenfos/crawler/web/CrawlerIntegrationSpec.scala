package uk.co.kenfos.crawler.web

import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest._
import uk.co.kenfos.crawler.domain.Url

class CrawlerIntegrationSpec extends AsyncWordSpec with Matchers with BeforeAndAfterEach with AsyncMockFactory {
  "Crawler" should {
    "make HTTP request and call scraper" in {
      val url = Url("http://www.kenfos.co.uk")
      val links = Set("http://www.kenfos.co.uk/about", "http://www.kenfos.co.uk/contact")
      val expectedLinks = Set(Url("http://www.kenfos.co.uk/about"), Url("http://www.kenfos.co.uk/contact"))
      val scraper = mock[Scraper]
      val urlBuilder = DefaultUrlBuilder
      val crawler = new HTTPCrawler(scraper, urlBuilder)
      scraper.getLinks _ expects * returning links
      crawler.crawl(url).map(response => response shouldBe expectedLinks)
    }

    "include root domain in relative links" in {
      val url = Url("http://www.kenfos.co.uk")
      val links = Set("http://www.kenfos.co.uk/about", "/contact")
      val expectedLinks = Set(Url("http://www.kenfos.co.uk/about"), Url("http://www.kenfos.co.uk/contact"))
      val scraper = mock[Scraper]
      val urlBuilder = DefaultUrlBuilder
      val crawler = new HTTPCrawler(scraper, urlBuilder)
      scraper.getLinks _ expects * returning links
      crawler.crawl(url).map(response => response shouldBe expectedLinks)
    }

    "filter out non web links" in {
      val url = Url("http://www.kenfos.co.uk")
      val links = Set("http://www.kenfos.co.uk/about", "mailto:davidmolinero.com@gmail.com")
      val expectedLinks = Set(Url("http://www.kenfos.co.uk/about"))
      val scraper = mock[Scraper]
      val urlBuilder = DefaultUrlBuilder
      val crawler = new HTTPCrawler(scraper, urlBuilder)
      scraper.getLinks _ expects * returning links
      crawler.crawl(url).map(response => response shouldBe expectedLinks)
    }

    "handle redirects" in {
      val url = Url("http://www.thoughtworks.com")
      val links = Set("http://www.thoughtworks.com/about-us")
      val expectedLinks = Set(Url("http://www.thoughtworks.com/about-us"))
      val scraper = mock[Scraper]
      val urlBuilder = DefaultUrlBuilder
      val crawler = new HTTPCrawler(scraper, urlBuilder)
      scraper.getLinks _ expects * returning links
      crawler.crawl(url).map(response => response shouldBe expectedLinks)
    }
  }
}
