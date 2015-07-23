package io.coding.me.m2p2.core.internal.model

/**
 * Naive representation of a Maven artifact coordinate
 */
case class MavenGAV(group: String, artifact: String, version: String, classifier: Option[String]) {
  require(group != null && !group.isEmpty(), "Value group of Maven GAV must not be empty")
  require(artifact != null && !artifact.isEmpty(), "Value artifact of Maven GAV must not be empty")
  require(version != null && !version.isEmpty(), "Value version of Maven GAV must not be empty")
}

