package uk.co.kenfos.crawler.domain

case class SiteGraph(rootDomain: String = "", urls: Map[Node, List[Node]] = Map.empty) {
  def contains(url: String): Boolean = urls.contains(Node(url))
  def add(node: String, nodes: List[String]): SiteGraph = SiteGraph(rootDomain, urls + (Node(node) -> nodes.map(Node)))
}

case class Node(url: String)