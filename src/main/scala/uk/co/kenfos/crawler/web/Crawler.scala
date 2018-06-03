package uk.co.kenfos.crawler.web

import com.typesafe.scalalogging.LazyLogging
import dispatch._
import org.asynchttpclient.Response
import uk.co.kenfos.crawler.domain.Url

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Crawler {
  def crawl(domainUrl: Url): Future[Set[Url]] = crawl(domainUrl, domainUrl)
  def crawl(domainUrl: Url, url: Url): Future[Set[Url]]
  def terminate(): Unit
}

class HTTPCrawler(scraper: Scraper, urlBuilder: UrlBuilder) extends Crawler with LazyLogging {

  private val httpClient = Http.default.closeAndConfigure(_ setFollowRedirect true)
  private val HTTP_OK = 200

  override def crawl(domainUrl: Url, url: Url): Future[Set[Url]] = {
    val request = dispatch.url(url.value).GET
    logger.info(s"crawling ${url}")
    httpClient(request).flatMap(response => handleResponse(domainUrl, url, response))
  }

  override def terminate(): Unit = httpClient.shutdown

  private def handleResponse(domainUrl: Url, url: Url, response: Response): Future[Set[Url]] = {
    if (HTTP_OK.equals(response.getStatusCode)) getLinks(domainUrl, url, response.getResponseBody) else Future { Set() }
  }

  private def getLinks(domainUrl: Url, url: Url, html: String): Future[Set[Url]] = {
    scraper.getLinks(html).map(urls => urls.flatMap(url => urlBuilder.build(domainUrl, url)))
  }
}