package uk.co.kenfos.crawler.actorsystem

import uk.co.kenfos.crawler.domain.Url

case class State(crawledUrls: Set[Url], siteMap: Map[Url, Set[Url]], toBeCrawled: Int) {
  def withNewCrawled(url: Url): State = State(crawledUrls + url, siteMap, toBeCrawled)
  def withNewSiteMap(url: Url, links: Set[Url], pendingToBeCrawled: Int): State = {
    State(crawledUrls ++ links, siteMap + (url -> links), pendingToBeCrawled)
  }
}

object State {
  def empty = State(Set.empty, Map.empty, 0)
  def initial = State(Set.empty, Map.empty, 1)
}