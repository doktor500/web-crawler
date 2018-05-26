package uk.co.kenfos.crawler.domain

case class SiteGraph(rootDomain: String = "", urls: Map[Node, Set[Node]] = Map.empty) {
  def contains(url: String): Boolean = urls.contains(Node(url)) || ulrExistsInNodes(url, urls) || rootDomain.equals(url)
  def add(node: String, nodes: Set[String]): SiteGraph = SiteGraph(rootDomain, urls + (Node(node) -> nodes.map(Node)))

  private def ulrExistsInNodes(url: String, urls: Map[Node, Set[Node]]): Boolean = {
    urls.keySet.exists(key => urls.getOrElse(key, Set()).contains(Node(url)))
  }
}

case class Node(url: String)