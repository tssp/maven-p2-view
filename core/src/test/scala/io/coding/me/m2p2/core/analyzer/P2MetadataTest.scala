package io.coding.me.m2p2.core.analyzer

import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import java.io.File
import scala.io.Source
import scala.util.Success

class P2MetadataTest extends WordSpecLike with Matchers {

  "A valid P2 content file" should {

    "not return a failure when parsing a file" in {

      val p2metadata = P2Metadata(new File(getClass.getResource("/reference_files/example-bundle-2.0.0-20150528.145642-258-p2metadata.xml").toURI()))

      p2metadata.isFailure shouldBe false
      p2metadata.get.isDefined shouldBe true
      p2metadata.get.get.size shouldBe 1
    }

  }

  "An invalid P2 content file" should {

    "return an exception when accessing an non-existing file" in {

      val p2unit = P2Metadata(new File("????"))

      assert(p2unit.isFailure)
    }
  }

  "An artifact analyzer " should {

    "extract version from file name" in {

      P2Metadata.extractMavenVersion(new File("example-bundle-2.0.0-20150528.145642-258-p2metadata.xml")) shouldBe Success("2.0.0-20150528.145642-258")
      P2Metadata.extractMavenVersion(new File("x.y.z-2.0.0-20150602.100144-218-p2metadata.xml")) shouldBe Success("2.0.0-20150602.100144-218")
    }
  }
}