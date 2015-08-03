package io.coding.me.m2p2.core.analyzer

import java.io.File

import org.scalatest.Matchers
import org.scalatest.WordSpecLike

class P2ArtifactTest extends WordSpecLike with Matchers {

  "A valid P2 artifacts file" should {

    "not return a failure when parsing a file" in {

      val p2artifacts = P2Artifact(new File(getClass.getResource("/reference_files/p2artifacts.xml").toURI()))

      assert(p2artifacts.isSuccess)
      assert(p2artifacts.get.size == 1)
     
      val refMavenGAV= MavenGAV("example.group", "example-bundle", "0.1.0-SNAPSHOT", None, None)
      val refArtifact= P2Artifact.P2Artifact("example-bundle", "0.1.0.201507201658", Some("osgi.bundle"), refMavenGAV)
      
      p2artifacts.get.head shouldBe refArtifact
    }
  }

  "An invalid P2 artifacts file" should {

    "return an exception when accessing an non-existing file" in {

      val p2artifact = P2Artifact(new File("????"))

      assert(p2artifact.isFailure)
    }
  }
}