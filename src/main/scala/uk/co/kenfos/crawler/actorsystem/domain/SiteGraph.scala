package uk.co.kenfos.crawler.actorsystem.domain

import uk.co.kenfos.crawler.domain.Url

case class SiteGraph(domainUrl: Url = Url.empty, graph: Map[Url, Set[Url]] = Map.empty) {
  def contains(url: Url): Boolean = domainUrl.equals(url) || graph.contains(url) || urlIsLinked(url, graph)
  def add(url: Url, links: Set[Url]): SiteGraph = SiteGraph(domainUrl, graph + (url -> links))

  private def urlIsLinked(url: Url, urls: Map[Url, Set[Url]]): Boolean = {
    urls.keySet.exists(key => urls.getOrElse(key, Set()).contains(url))
  }
}