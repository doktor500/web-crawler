package uk.co.kenfos.crawler.web

import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest._
import uk.co.kenfos.crawler.domain.Url

import scala.concurrent.Future

class CrawlerIntegrationSpec extends AsyncWordSpec with Matchers with BeforeAndAfterEach with AsyncMockFactory {
  "Crawler" should {
    "make HTTP request and call scraper" in {
      val url = Url("https://www.thoughtworks.com")
      val links = Set("https://www.thoughtworks.com/about-us")
      val expectedLinks = Set(Url("https://www.thoughtworks.com/about-us"))
      val scraper = mock[Scraper]
      val urlBuilder = DefaultUrlBuilder
      val crawler = new HTTPCrawler(scraper, urlBuilder)
      scraper.getLinks _ expects * returning Future { links }
      crawler.crawl(url).map(response => response shouldBe expectedLinks)
    }

    "include root domain in relative links" in {
      val url = Url("https://www.thoughtworks.com")
      val links = Set("/contact-us")
      val expectedLinks = Set(Url("https://www.thoughtworks.com/contact-us"))
      val scraper = mock[Scraper]
      val urlBuilder = DefaultUrlBuilder
      val crawler = new HTTPCrawler(scraper, urlBuilder)
      scraper.getLinks _ expects * returning Future { links }
      crawler.crawl(url).map(response => response shouldBe expectedLinks)
    }

    "filter out non web links" in {
      val url = Url("https://www.thoughtworks.com")
      val links = Set("https://www.thoughtworks.com/about-us", "mailto:davidmolinero.com@gmail.com")
      val expectedLinks = Set(Url("https://www.thoughtworks.com/about-us"))
      val scraper = mock[Scraper]
      val urlBuilder = DefaultUrlBuilder
      val crawler = new HTTPCrawler(scraper, urlBuilder)
      scraper.getLinks _ expects * returning Future { links }
      crawler.crawl(url).map(response => response shouldBe expectedLinks)
    }

    "handle redirects" in {
      val url = Url("https://www.thoughtworks.com")
      val links = Set("https://www.thoughtworks.com/about-us")
      val expectedLinks = Set(Url("https://www.thoughtworks.com/about-us"))
      val scraper = mock[Scraper]
      val urlBuilder = DefaultUrlBuilder
      val crawler = new HTTPCrawler(scraper, urlBuilder)
      scraper.getLinks _ expects * returning Future { links }
      crawler.crawl(url).map(response => response shouldBe expectedLinks)
    }
  }
}
