package uk.co.kenfos.crawler.web

import org.jsoup.Jsoup

import scala.collection.JavaConverters._

trait Scraper {
  def getLinks(html: String): Set[String]
}

object HTMLScraper extends Scraper {
  override def getLinks(html: String): Set[String] = {
    val document = Jsoup.parse(html)
    val links = document.select("a").eachAttr("href")
    links.asScala.toSet
  }
}