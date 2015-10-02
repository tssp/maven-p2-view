package io.coding.me.m2p2.core.actor.artifact

import io.coding.me.m2p2.core.analyzer.P2Unit

class P2MetadataAnalyzerTest extends ArtifactAnalyzerTest[P2Unit] {

  override lazy val analyzerActor = system.actorOf(ArtifactAnalyzer.p2metadataProps(repositoryId))
  override lazy val analyzerFile = getResourceFile("example-bundle-2.0.0-20150528.145642-258-p2metadata.xml")
}