package io.coding.me.m2p2.core.actor.artifact

import io.coding.me.m2p2.core.analyzer.P2Artifact
import java.io.File
import scala.util.Success

class P2ArtifactAnalyzerTest extends ArtifactAnalyzerTest[P2Artifact] {

  override lazy val analyzerActor = system.actorOf(ArtifactAnalyzer.p2artifactProps(repositoryId))
  override lazy val analyzerFile = getResourceFile("example-bundle-2.0.0-20150528.145642-258-p2artifacts.xml")
  
  "An artifact analyzer " should {

    "extract version from file name" in {

      P2Artifact.extractMavenVersion(new File("example-bundle-2.0.0-20150528.145642-258-p2artifacts.xml")) shouldBe Success("2.0.0-20150528.145642-258")
      P2Artifact.extractMavenVersion(new File("x.y.z-2.0.0-20150602.100144-218-p2artifacts.xml")) shouldBe Success("2.0.0-20150602.100144-218")
    }
  }
}