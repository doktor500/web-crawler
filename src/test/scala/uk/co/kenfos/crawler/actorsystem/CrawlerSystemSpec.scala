package uk.co.kenfos.crawler.actorsystem

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import uk.co.kenfos.crawler.actorsystem.CrawlerSystem.{GetState, Init}
import uk.co.kenfos.crawler.domain.{SiteGraph, Node}
import uk.co.kenfos.crawler.service.DefaultCrawler

import scala.concurrent.duration._

class CrawlerSystemSpec extends TestKit(ActorSystem("CrawlerSystem"))
  with WordSpecLike
  with Matchers
  with ScalaFutures
  with BeforeAndAfterAll {

  implicit val timeout = Timeout(1.seconds)

  override def afterAll() = {
    system.terminate
    super.afterAll()
  }

  "CrawlerSystem" should {
    "crawls a domain" in {
      val domainUrl = "www.kenfos.co.uk"
      val aboutUrl = "www.kenfos.co.uk/contact"
      val siteMap = Map(domainUrl -> List(aboutUrl), aboutUrl -> List())
      val crawlerSystem = TestActorRef.apply(new CrawlerSystem(new DefaultCrawler(siteMap)))

      crawlerSystem ! Init(domainUrl)

      val actorState = (crawlerSystem ? GetState).futureValue
      actorState.leftSideValue shouldBe SiteGraph(
        domainUrl,
        Map(Node(domainUrl) -> List(Node(aboutUrl)), Node(aboutUrl) -> List())
      )
    }
  }
}
