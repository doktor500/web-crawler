package uk.co.kenfos.crawler.web

import org.apache.commons.validator.routines.UrlValidator
import uk.co.kenfos.crawler.domain.Url

trait UrlBuilder {
  def build(domainUrl: Url, link: String): Option[Url]
}

object DefaultUrlBuilder extends UrlBuilder {

  private val urlValidator = new UrlValidator()

  def build(domainUrl: Url, link: String): Option[Url] = {
    if (link.contains("http")) toValidUrl(link)
    else if (link.contains(":") || link.contains("#")) None
    else toValidUrl(s"${domainUrl}$link")
  }

  private def toValidUrl(url: String): Option[Url] = {
    if (urlValidator.isValid(url.trim)) Some(Url(url.trim)) else None
  }
}
