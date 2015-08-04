package io.coding.me.m2p2.core.analyzer

import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import java.io.File
import scala.io.Source

class P2FeatureTest extends WordSpecLike with Matchers {

  "A valid P2 feature jar file" should {

    "not return a failure when parsing a file" in {

      val jarFile= new File(getClass.getResource("/reference_files/feature-0.1.0-SNAPSHOT.jar").toURI())
      val result = P2Feature(jarFile)

      result.isSuccess shouldBe true
      result.get.isDefined shouldBe true
      result.get.get.id shouldBe "example-feature"
      result.get.get.version shouldBe "0.1.0.201507201658"
      
      result.get.get.plugins should not be empty
      result.get.get.plugins.head.id shouldBe "example-bundle"
      result.get.get.plugins.head.version shouldBe "0.1.0.201507201658"

    }

  }

  "An invalid P2 content file" should {

    "return an exception when accessing an non-existing file" in {

      P2Metadata(new File("????")).isFailure shouldBe true
    }
    
    "return none when accessing an jar file without feature.xml" in {

      val jarFile= new File(getClass.getResource("/reference_files/feature-invalid-0.1.0-SNAPSHOT.jar").toURI())
      val result=P2Feature(jarFile) 
      
      result.isSuccess shouldBe true
      result.get.isDefined shouldBe false
    }
  }
}