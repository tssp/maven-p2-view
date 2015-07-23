package io.coding.me.m2p2.core.internal.model

import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import java.io.File
import scala.io.Source

/**
 * @author tim@coding-me.com
 */
class P2UnitTest extends WordSpecLike with Matchers {
  
  "A valid P2 content file" should {
    
    "not return a failure when parsing a file" in {
      
      val p2unit= P2Unit(new File(getClass.getResource("/reference_files/p2content.xml").toURI()))
      
      assert(p2unit.isSuccess)
      assert(p2unit.get.size == 1)
      assert(p2unit.get.head.artifacts.size == 1)
    }
    
    
  }
  
  "An invalid P2 content file" should {
    
    "return an exception when accessing an non-existing file" in {
      
      val p2unit= P2Unit(new File("????"))
      
      assert(p2unit.isFailure)
    }
  }
}