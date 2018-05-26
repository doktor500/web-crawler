package uk.co.kenfos

import akka.actor.{ActorSystem, Props}
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.Init
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem
import uk.co.kenfos.crawler.service.{HTMLScraper, HTTPCrawler}

object Main extends App {

  val domainUrl = "http://www.kenfos.co.uk"
  val aboutUrl = "http://www.kenfos.co.uk/about"
  val siteMap = Map(domainUrl -> List(aboutUrl), aboutUrl -> List())

  val system = ActorSystem("CrawlerSystem")
  val crawler = system.actorOf(Props(new CrawlerSystem(new HTTPCrawler(HTMLScraper))))

  crawler ! Init(domainUrl)
}