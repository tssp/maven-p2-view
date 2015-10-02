package io.coding.me.m2p2.core.analyzer

import java.io.File
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import scala.util.Success

class P2ArtifactTest extends WordSpecLike with Matchers {

  "A valid P2 artifacts file" should {

    "not return a failure when parsing a file" in {

      val p2artifacts = P2Artifact(new File(getClass.getResource("/reference_files/example-bundle-2.0.0-20150528.145642-258-p2artifacts.xml").toURI()))

      p2artifacts.isFailure shouldBe false
      p2artifacts.get.isDefined shouldBe true
      p2artifacts.get.get.size shouldBe 1
      
     
      val refMavenGAV= MavenGAV("example.group", "example-bundle", "2.0.0-20150528.145642-258", None, None)
      val refArtifact= P2Artifact("example-bundle", "0.1.0.201507201658", Some("osgi.bundle"), refMavenGAV)
      
      p2artifacts.get.get.head shouldBe refArtifact
    }
  }

  "An invalid P2 artifacts file" should {

    "return an exception when accessing an non-existing file" in {

      val p2artifact = P2Artifact(new File("????"))

      assert(p2artifact.isFailure)
    }
  }
  
  "An artifact analyzer " should {

    "extract version from file name" in {

      P2Artifact.extractMavenVersion(new File("example-bundle-2.0.0-20150528.145642-258-p2artifacts.xml")) shouldBe Success("2.0.0-20150528.145642-258")
      P2Artifact.extractMavenVersion(new File("x.y.z-2.0.0-20150602.100144-218-p2artifacts.xml")) shouldBe Success("2.0.0-20150602.100144-218")
    }
  }
}