package uk.co.kenfos.crawler.actorsystem.domain

import uk.co.kenfos.crawler.domain.Url

case class SiteGraph(domainUrl: Url = Url(""), urls: Map[Url, Set[Url]] = Map.empty) {
  def contains(url: Url): Boolean = domainUrl.equals(url) || urls.contains(url) || urlIsLinked(url, urls)
  def add(url: Url, links: Set[Url]): SiteGraph = SiteGraph(domainUrl, urls + (url -> links))

  private def urlIsLinked(url: Url, urls: Map[Url, Set[Url]]): Boolean = {
    urls.keySet.exists(key => urls.getOrElse(key, Set()).contains(url))
  }
}