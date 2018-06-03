package uk.co.kenfos.crawler.web

import org.jsoup.Jsoup

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Scraper {
  def getLinks(html: String): Future[Set[String]]
}

object HTMLScraper extends Scraper {
  override def getLinks(html: String): Future[Set[String]] = Future {
    val document = Jsoup.parse(html)
    val links = document.select("a").eachAttr("href")
    links.asScala.toSet
  }
}