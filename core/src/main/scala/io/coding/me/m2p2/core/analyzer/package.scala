package io.coding.me.m2p2.core

package analyzer {

  /**
   * Naive value representation of a Maven GAV
   */
  case class MavenGAV(groupId: String, artifactId: String, version: String, classifier: Option[String], extension: Option[String])
}