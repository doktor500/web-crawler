package uk.co.kenfos.crawler.actorsystem.domain

import uk.co.kenfos.crawler.domain.Url

case class SiteGraph(domainUrl: Url = Url.empty, graph: Map[Url, Set[Url]] = Map.empty, urls: Set[Url] = Set.empty) {
  def contains(url: Url): Boolean = urls.contains(url)
  def add(url: Url, links: Set[Url]): SiteGraph = SiteGraph(domainUrl, graph + (url -> links), urls ++ links + url)
}

object SiteGraph {
  def empty: SiteGraph = SiteGraph(Url.empty, Map.empty, Set.empty)
  def init(domainUrl: Url): SiteGraph = SiteGraph(domainUrl, Map.empty, Set(domainUrl))
}