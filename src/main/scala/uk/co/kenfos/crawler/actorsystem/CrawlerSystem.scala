package uk.co.kenfos.crawler.actorsystem

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem._
import uk.co.kenfos.crawler.domain.Url
import uk.co.kenfos.crawler.web.{Crawler, Serializer}

import scala.util.{Failure, Success}

class CrawlerSystem(domainUrl: Url, crawler: Crawler, siteMapSerializer: Serializer) extends Actor with LazyLogging {
  import context.dispatcher

  private val timeout = "timed out"

  def receive: Receive = active(State.empty)

  def active(state: State): Receive = {
    case Init                         => init()
    case CrawlRequest(url)            => triggerCrawlRequest(url, state)
    case CrawlResponse(url, newLinks) => processCrawlResponse(url, newLinks, state)
    case GetState                     => sender() ! state.crawledUrls
    case Shutdown                     => shutdown(state)
  }

  private def init(): Unit = {
    logger.info("Actor system initiated")
    context.become(active(State.initial))
    self ! CrawlRequest(domainUrl)
  }

  private def shutdown(state: State): Unit = {
    logger.info(s"Number of URLs processed: ${state.crawledUrls.size}")
    logger.info("Terminating actor system")
    siteMapSerializer.serialize(state.siteMap)
    crawler.terminate()
    context.system.terminate()
  }

  private def triggerCrawlRequest(url: Url, state: State): Unit = {
    context.become(active(state.withNewCrawled(url)))
    crawler.crawl(domainUrl, url).onComplete {
      case Success(links) => self ! CrawlResponse(url, links)
      case Failure(e)     => if (e.getMessage.contains(timeout)) self ! CrawlRequest(url) else self ! CrawlResponse(url)
    }
  }

  private def processCrawlResponse(url: Url, newLinks: Set[Url], state: State): Unit = {
    val newLinksToBeCrawled = newLinks.filter(link => link.hasSameHostAs(url) && !state.crawledUrls.contains(link))
    val linksPendingToBeCrawled = state.toBeCrawled + newLinksToBeCrawled.size - 1
    context.become(active(state.withNewSiteMap(url, newLinksToBeCrawled, linksPendingToBeCrawled)))
    newLinksToBeCrawled.foreach(link => self ! CrawlRequest(link))
    if (linksPendingToBeCrawled == 0) self ! Shutdown
  }
}

object CrawlerSystem {
  case object Init
  case object GetState
  case class CrawlRequest(url: Url)
  case class CrawlResponse(url: Url, links: Set[Url] = Set())
  case object Shutdown
}
