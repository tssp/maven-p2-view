package io.coding.me.m2p2.core.internal.metric

class ArtifactCollectorMetrics(repo: String, group: String) {
  val queueSize = Registry().counter(s"${repo}.${group}.queue-size")
  val operations = Registry().meter(s"${repo}.${group}.operations")

}

object ArtifactCollectorMetrics {
  def insert(repo: String) = new ArtifactCollectorMetrics(repo, "insert-artifact")
  def delete(repo: String) = new ArtifactCollectorMetrics(repo, "delete-artifact")
}