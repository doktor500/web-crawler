package uk.co.kenfos.crawler.service

import com.typesafe.scalalogging.LazyLogging
import dispatch._
import uk.co.kenfos.crawler.domain.Url

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Crawler {
  def crawl(domainUrl: Url): Future[Set[Url]] = crawl(domainUrl, domainUrl)
  def crawl(domainUrl: Url, url: Url): Future[Set[Url]]
  def terminate(): Unit
}

class HTTPCrawler(scraper: Scraper, urlBuilder: UrlBuilder) extends Crawler with LazyLogging {

  override def crawl(domainUrl: Url, url: Url): Future[Set[Url]] = {
    val request = dispatch.url(url.value).GET
    logger.info(s"crawling ${url.value}")
    Http.default(request).map(response => getLinks(domainUrl, response.getResponseBody))
  }

  override def terminate(): Unit = Http.default.shutdown

  private def getLinks(domainUrl: Url, html: String): Set[Url] = {
    scraper.getLinks(html).flatMap(url => urlBuilder.build(domainUrl, url))
  }
}