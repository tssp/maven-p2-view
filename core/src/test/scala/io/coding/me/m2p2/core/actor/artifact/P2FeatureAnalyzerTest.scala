package io.coding.me.m2p2.core.actor.artifact

import io.coding.me.m2p2.core.analyzer.P2Unit

class P2FeatureAnalyzerTest extends ArtifactAnalyzerTest[P2Unit] {

  override lazy val analyzerActor = system.actorOf(ArtifactAnalyzer.p2featureJarProps(repositoryId))
  override lazy val analyzerFile = getResourceFile("feature-0.1.0-SNAPSHOT.jar")
}