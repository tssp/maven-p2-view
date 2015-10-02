package io.coding.me.m2p2.core.actor.artifact

import io.coding.me.m2p2.core.analyzer.P2Artifact

class P2ArtifactAnalyzerTest extends ArtifactAnalyzerTest[P2Artifact] {

  override lazy val analyzerActor = system.actorOf(ArtifactAnalyzer.p2artifactProps(repositoryId))
  override lazy val analyzerFile = getResourceFile("p2artifacts.xml")
}