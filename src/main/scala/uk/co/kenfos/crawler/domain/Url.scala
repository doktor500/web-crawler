package uk.co.kenfos.crawler.domain

import java.net.URL

case class Url(value: String) {
  def hasSameHostAs(other: Url): Boolean = new URL(value).getHost.equals(new URL(other.value).getHost)
  override def toString(): String = value
}

object Url {
  def empty = Url("")
}