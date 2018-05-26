package uk.co.kenfos.crawler.service

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Crawler {
  def crawl(url: String): Future[List[String]]
}

class DefaultCrawler(siteMap: Map[String, List[String]]) extends Crawler {
  def crawl(url: String): Future[List[String]] = Future { siteMap.getOrElse(url, List()) }
}
