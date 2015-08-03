package io.coding.me.m2p2.core.analyzer

import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import java.io.File

class MavenMetadataTest extends WordSpecLike with Matchers {

  "A valid Maven metadata file" should {

    "not return a failure when parsing a file" in {

      val mavenMetadata = MavenMetadata(new File(getClass.getResource("/reference_files/maven-metadata.xml").toURI()))

      mavenMetadata should not be None
      mavenMetadata.get should not be empty
      mavenMetadata.get.size shouldBe 4
      mavenMetadata.get.head shouldBe MavenGAV("io.coding-me", "test", "1.4.0-20150723.181352-1", Some("javadoc"), Some("jar"))

    }
  }

}