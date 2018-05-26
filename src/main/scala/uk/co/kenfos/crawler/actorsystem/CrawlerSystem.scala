package uk.co.kenfos.crawler.actorsystem

import akka.actor.Actor
import akka.pattern.pipe
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.{CrawlRequest, CrawlResponse, GetState, Init}
import uk.co.kenfos.crawler.domain.SiteGraph
import uk.co.kenfos.crawler.service.Crawler

class CrawlerSystem(crawler: Crawler) extends Actor {

  import context.dispatcher

  def receive: Receive = active(SiteGraph())

  def active(siteGraph: SiteGraph): Receive = {
    case Init(domain)              => init(domain)
    case CrawlRequest(url)         => triggerCrawlRequest(url, siteGraph)
    case CrawlResponse(url, links) => processCrawlResponse(url, links, siteGraph)
    case GetState                  => sender() ! siteGraph
  }

  private def init(domain: String): Unit = {
    context.become(active(SiteGraph(domain)))
    self ! CrawlRequest(domain)
  }

  private def triggerCrawlRequest(url: String, siteGraph: SiteGraph): Unit = {
    context.become(active(siteGraph.add(url, Set())))
    crawler.crawl(siteGraph.rootDomain, url)
      .map(urls => CrawlResponse(url, urls))
      .pipeTo(self)(sender())
  }

  private def processCrawlResponse(url: String, links: Set[String], siteGraph: SiteGraph): Unit = {
    val newLinks = links.filter(link => link.startsWith(siteGraph.rootDomain) && !siteGraph.contains(link))
    val newGraph = siteGraph.add(url, newLinks)
    context.become(active(newGraph))
    newLinks.foreach(link => triggerCrawlRequest(link, newGraph))
  }
}

object CrawlerSystem {
  case object GetState
  case class Init(domain: String)
  case class CrawlRequest(url: String)
  case class CrawlResponse(url: String, links: Set[String])
}
