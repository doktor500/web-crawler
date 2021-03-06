package uk.co.kenfos.crawler.actorsystem

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.{GetState, Init}
import uk.co.kenfos.crawler.domain.Url
import uk.co.kenfos.crawler.web.{Crawler, JsonSerializer}

import scala.concurrent.Future
import scala.concurrent.duration._

class CrawlerSystemSpec extends TestKit(ActorSystem("CrawlerSystem"))
  with WordSpecLike
  with Matchers
  with ScalaFutures
  with BeforeAndAfterAll {

  implicit val timeout: Timeout = Timeout(1.seconds)

  override def afterAll(): Unit = system.terminate

  "CrawlerSystem" should {
    "crawl a single domain" in {
      val domainUrl = Url("https://www.thoughtworks.com")
      val aboutUrl = Url("https://www.thoughtworks.com/contact-us")
      val distinctHostUrl = Url("http://google.com")
      val siteMap = Map(domainUrl -> Set(aboutUrl), aboutUrl -> Set[Url](), distinctHostUrl -> Set[Url]())
      val crawlerSystem = TestActorRef.apply(new CrawlerSystem(domainUrl, new FakeCrawler(siteMap), JsonSerializer))

      crawlerSystem ! Init

      val actorState = (crawlerSystem ? GetState).futureValue
      actorState.leftSideValue shouldBe Set(domainUrl, aboutUrl)
    }
  }
}

class FakeCrawler(siteMap: Map[Url, Set[Url]]) extends Crawler {
  import scala.concurrent.ExecutionContext.Implicits.global
  override def crawl(domainUrl: Url, url: Url): Future[Set[Url]] = Future { siteMap.getOrElse(url, Set()) }
  override def terminate(): Unit = println("")
}

