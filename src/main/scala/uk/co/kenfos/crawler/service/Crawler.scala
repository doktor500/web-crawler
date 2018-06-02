package uk.co.kenfos.crawler.service

import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import dispatch._

trait Crawler {
  def crawl(domain: String, url: String): Future[Set[String]]
  def terminate: Unit
}

class HTTPCrawler(scraper: Scraper) extends Crawler with LazyLogging {

  override def crawl(domain: String, url: String): Future[Set[String]] = {
    val request = dispatch.url(url).GET
    logger.info(s"crawling $url")
    Http.default(request).map(response => getLinks(domain, response.getResponseBody))
  }

  override def terminate = Http.default.shutdown

  private def getLinks(domain: String, html: String) = {
    scraper.getLinks(html).flatMap(link => {
      if (link.contains("http")) Some(link)
      else if (link.contains(":")) None
      else Some(s"$domain$link")
    })
  }
}