package io.coding.me.util.p2.core.internal.model

import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import java.io.File

/**
 * @author tim@coding-me.com
 */
class P2ArtifactTest extends WordSpecLike with Matchers {
  
  "A valid P2 artifacts file" should {
    
    "not return a failure when parsing a file" in {
      
      val p2artifact= P2Artifact(new File(getClass.getResource("/reference_files/p2artifacts.xml").toURI()))
      
      assert(p2artifact.isSuccess)
      assert(p2artifact.get.size == 1)
    }
    
    
  }
  
  "An invalid P2 artifacts file" should {
    
    "return an exception when accessing an non-existing file" in {
      
      val p2artifact= P2Artifact(new File("????"))
      
      assert(p2artifact.isFailure)
    }
  }
}