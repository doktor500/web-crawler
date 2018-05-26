package uk.co.kenfos.crawler.service

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

trait Crawler {
  def crawl(domain: String, url: String): Future[Set[String]]
}

class HTTPCrawler(scraper: Scraper) extends Crawler with LazyLogging {
  override def crawl(domain: String, url: String): Future[Set[String]] = Future {
    logger.debug(s"crawling $url")
    val response = Source.fromURL(url).mkString
    scraper.getLinks(response).map(link => if (link.contains("http")) link else s"$domain$link")
  }
}