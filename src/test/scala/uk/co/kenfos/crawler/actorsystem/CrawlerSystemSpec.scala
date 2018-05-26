package uk.co.kenfos.crawler.actorsystem

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.{GetState, Init}
import uk.co.kenfos.crawler.domain.{Node, SiteGraph}
import uk.co.kenfos.crawler.service.Crawler

import scala.concurrent.Future
import scala.concurrent.duration._

class CrawlerSystemSpec extends TestKit(ActorSystem("CrawlerSystem"))
  with WordSpecLike
  with Matchers
  with ScalaFutures
  with BeforeAndAfterAll {

  implicit val timeout: Timeout = Timeout(1.seconds)

  override def afterAll() = {
    system.terminate
  }

  "CrawlerSystem" should {
    "crawls a domain" in {
      val domainUrl = "http://www.kenfos.co.uk"
      val aboutUrl = "http://www.kenfos.co.uk/contact"
      val siteMap = Map(domainUrl -> Set(aboutUrl), aboutUrl -> Set[String]())
      val crawlerSystem = TestActorRef.apply(new CrawlerSystem(new FakeCrawler(siteMap)))

      crawlerSystem ! Init(domainUrl)

      val actorState = (crawlerSystem ? GetState).futureValue
      actorState.leftSideValue shouldBe SiteGraph(
        domainUrl,
        Map(Node(domainUrl) -> Set(Node(aboutUrl)), Node(aboutUrl) -> Set())
      )
    }
  }
}

class FakeCrawler(siteMap: Map[String, Set[String]]) extends Crawler {
  import scala.concurrent.ExecutionContext.Implicits.global
  override def crawl(domain: String, url: String): Future[Set[String]] = Future { siteMap.getOrElse(url, Set()) }
}

