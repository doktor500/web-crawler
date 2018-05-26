package uk.co.kenfos

import akka.actor.{ActorSystem, Props}
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.Init
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem
import uk.co.kenfos.crawler.service.DefaultCrawler

object Main extends App {

  val domainUrl = "www.kenfos.co.uk"
  val aboutUrl = "www.kenfos.co.uk/about"
  val siteMap = Map(domainUrl -> List(aboutUrl), aboutUrl -> List())

  val system = ActorSystem("CrawlerSystem")
  val crawler = system.actorOf(Props(new CrawlerSystem(new DefaultCrawler(siteMap))))

  crawler ! Init(domainUrl)
}