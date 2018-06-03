package uk.co.kenfos.crawler.actorsystem

import akka.actor.{Actor, ReceiveTimeout}
import akka.pattern.pipe
import com.typesafe.scalalogging.LazyLogging
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem._
import uk.co.kenfos.crawler.actorsystem.domain.SiteGraph
import uk.co.kenfos.crawler.domain.Url
import uk.co.kenfos.crawler.web.{Crawler, Serializer}

import scala.concurrent.duration._

class CrawlerSystem(crawler: Crawler, serializer: Serializer) extends Actor with LazyLogging {

  import context.dispatcher

  context.setReceiveTimeout(60 seconds)

  def receive: Receive = active(SiteGraph.empty, 0)

  def active(siteGraph: SiteGraph, count: Int): Receive = {
    case Init(domain)              => init(Url(domain))
    case CrawlRequest(url)         => triggerCrawlRequest(url, siteGraph, count)
    case CrawlResponse(url, links) => processCrawlResponse(url, links, siteGraph, count)
    case GetState                  => sender() ! siteGraph
    case ReceiveTimeout            => handleTimeout(siteGraph)
    case Shutdown                  => shutdown(siteGraph)
  }

  private def init(domain: Url): Unit = {
    logger.info("Initialising actor system")
    context.become(active(SiteGraph.init(domain), 1))
    self ! CrawlRequest(domain)
  }

  private def handleTimeout(siteGraph: SiteGraph): Unit = {
    logger.warn("Terminating due to timeout")
    shutdown(siteGraph)
  }

  private def shutdown(siteGraph: SiteGraph): Unit = {
    logger.info(s"Number of pages processed: ${siteGraph.graph.keySet.size}")
    logger.info("Terminating actor system")
    serializer.serialize(siteGraph.graph)
    crawler.terminate()
    context.system.terminate()
  }

  private def triggerCrawlRequest(url: Url, siteGraph: SiteGraph, count: Int): Unit = {
    context.become(active(siteGraph.add(url, Set()), count))
    crawler.crawl(siteGraph.domainUrl, url)
      .map(urls => CrawlResponse(url, urls))
      .pipeTo(self)(sender())
  }

  private def processCrawlResponse(url: Url, links: Set[Url], siteGraph: SiteGraph, count: Int): Unit = {
    val newLinks = links.filter(link => link.hasSameHostAs(url) && !siteGraph.contains(link))
    val newGraph = siteGraph.add(url, newLinks)
    val linksPendingToBeProcessed = count + newLinks.size - 1
    context.become(active(newGraph, linksPendingToBeProcessed))
    newLinks.foreach(link => self ! CrawlRequest(link))
    if (linksPendingToBeProcessed == 0) self ! Shutdown
  }
}

object CrawlerSystem {
  case object GetState
  case class Init(domain: String)
  case class CrawlRequest(url: Url)
  case class CrawlResponse(url: Url, links: Set[Url])
  case object Shutdown
}
