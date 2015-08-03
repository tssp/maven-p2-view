package io.coding.me.m2p2.core.analyzer

import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import java.io.File
import scala.io.Source

class P2MetadataTest extends WordSpecLike with Matchers {

  "A valid P2 content file" should {

    "not return a failure when parsing a file" in {

      val p2metadata = P2Metadata(new File(getClass.getResource("/reference_files/p2content.xml").toURI()))

      assert(p2metadata.isSuccess)
      assert(p2metadata.get.size == 1)
    }

  }

  "An invalid P2 content file" should {

    "return an exception when accessing an non-existing file" in {

      val p2unit = P2Metadata(new File("????"))

      assert(p2unit.isFailure)
    }
  }
}