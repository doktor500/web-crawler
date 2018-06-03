package uk.co.kenfos

import akka.actor.{ActorSystem, Props}
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.Init
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem
import uk.co.kenfos.crawler.domain.Url
import uk.co.kenfos.crawler.web.{DefaultUrlBuilder, HTMLScraper, HTTPCrawler, JsonSerializer}

object Main extends App {

  val domainUrl = args(0)
  val system = ActorSystem("CrawlerSystem")
  val httpCrawler = new HTTPCrawler(HTMLScraper, DefaultUrlBuilder)
  val crawler = system.actorOf(Props(new CrawlerSystem(Url(domainUrl), httpCrawler, JsonSerializer)))

  crawler ! Init
}