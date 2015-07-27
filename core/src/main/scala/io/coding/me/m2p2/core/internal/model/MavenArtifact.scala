package io.coding.me.m2p2.core.internal.model

/**
 * Naive representation of a Maven artifact coordinate
 */
case class MavenArtifact(group: String, artifact: String, version: String, extension: String, classifier: Option[String]) {

  import io.coding.me.m2p2.core.internal.extension.StringExtensions._

  require(group.isNotNullOrEmpty(), "Value group of Maven GAV must not be empty")
  require(artifact.isNotNullOrEmpty(), "Value artifact of Maven GAV must not be empty")
  require(version.isNotNullOrEmpty(), "Value version of Maven GAV must not be empty")
  require(extension.isNotNullOrEmpty(), "Value extension of Maven GAV must not be empty")
}




