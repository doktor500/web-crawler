package uk.co.kenfos.crawler.actorsystem

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.{GetState, Init}
import uk.co.kenfos.crawler.actorsystem.domain.SiteGraph
import uk.co.kenfos.crawler.domain.Url
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
    "crawl a single domain" in {
      val domainUrl = Url("http://www.kenfos.co.uk")
      val aboutUrl = Url("http://www.kenfos.co.uk/contact")
      val distinctHostUrl = Url("http://google.com")
      val siteMap = Map(domainUrl -> Set(aboutUrl), aboutUrl -> Set[Url](), distinctHostUrl -> Set[Url]())
      val crawlerSystem = TestActorRef.apply(new CrawlerSystem(new FakeCrawler(siteMap)))

      crawlerSystem ! Init(domainUrl.value)

      val actorState = (crawlerSystem ? GetState).futureValue
      actorState.leftSideValue shouldBe SiteGraph(
        domainUrl,
        Map(domainUrl -> Set(aboutUrl), aboutUrl -> Set())
      )
    }
  }
}

class FakeCrawler(siteMap: Map[Url, Set[Url]]) extends Crawler {
  import scala.concurrent.ExecutionContext.Implicits.global
  override def crawl(domainUrl: Url, url: Url): Future[Set[Url]] = Future { siteMap.getOrElse(url, Set()) }
  override def terminate(): Unit = println("terminate")
}

