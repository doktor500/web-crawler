package uk.co.kenfos.crawler.actorsystem

import akka.actor.{Actor, ReceiveTimeout}
import akka.pattern.pipe
import com.typesafe.scalalogging.LazyLogging
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.{CrawlRequest, CrawlResponse, GetState, Init}
import uk.co.kenfos.crawler.actorsystem.domain.SiteGraph
import uk.co.kenfos.crawler.domain.Url
import uk.co.kenfos.crawler.web.{Crawler, Serializer}

import scala.concurrent.duration._

class CrawlerSystem(crawler: Crawler, serializer: Serializer) extends Actor with LazyLogging {

  import context.dispatcher

  context.setReceiveTimeout(2 seconds)

  def receive: Receive = active(SiteGraph.empty)

  def active(siteGraph: SiteGraph): Receive = {
    case Init(domain)              => init(Url(domain))
    case CrawlRequest(url)         => triggerCrawlRequest(url, siteGraph)
    case CrawlResponse(url, links) => processCrawlResponse(url, links, siteGraph)
    case GetState                  => sender() ! siteGraph
    case ReceiveTimeout            => shutdown(siteGraph)
  }

  private def init(domain: Url): Unit = {
    logger.info("Initialising actor system")
    context.become(active(SiteGraph.init(domain)))
    self ! CrawlRequest(domain)
  }

  private def shutdown(siteGraph: SiteGraph): Unit = {
    logger.info(s"Number of pages processed: ${siteGraph.graph.keySet.size}")
    logger.info("Terminating actor system")
    crawler.terminate()
    context.system.terminate()
    serializer.serialize(siteGraph.graph)
  }

  private def triggerCrawlRequest(url: Url, siteGraph: SiteGraph): Unit = {
    context.become(active(siteGraph.add(url, Set())))
    crawler.crawl(siteGraph.domainUrl, url)
      .map(urls => CrawlResponse(url, urls))
      .pipeTo(self)(sender())
  }

  private def processCrawlResponse(url: Url, links: Set[Url], siteGraph: SiteGraph): Unit = {
    val newLinks = links.filter(link => link.hasSameHostAs(url) && !siteGraph.contains(link))
    val newGraph = siteGraph.add(url, newLinks)
    context.become(active(newGraph))
    newLinks.foreach(link => triggerCrawlRequest(link, newGraph))
  }
}

object CrawlerSystem {
  case object GetState
  case class Init(domain: String)
  case class CrawlRequest(url: Url)
  case class CrawlResponse(url: Url, links: Set[Url])
}
