package uk.co.kenfos

import akka.actor.{ActorSystem, Props}
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.Init
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem
import uk.co.kenfos.crawler.service.{DefaultUrlBuilder, HTMLScraper, HTTPCrawler}

object Main extends App {

  val domainUrl = "http://www.kenfos.co.uk"
  val system = ActorSystem("CrawlerSystem")
  val crawler = system.actorOf(Props(new CrawlerSystem(new HTTPCrawler(HTMLScraper, DefaultUrlBuilder))))

  crawler ! Init(domainUrl)
}