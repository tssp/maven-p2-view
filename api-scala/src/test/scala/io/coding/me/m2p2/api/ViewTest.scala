package io.coding.me.m2p2.api

package io.coding.me.m2p2.core.actor

import org.scalatest._
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures

class ViewTest extends WordSpecLike with Matchers with ScalaFutures {

  "A true P2 view on an existing Maven repository" should {
    
    var view: View= null
    
    "be creatable with absolutely no effort" in {
      
      view= View()
      
      view should not be (null)
    }
    
    "not have any repository after creation" in {
      
      view.getRepositories().futureValue shouldBe defined
      view.getRepositories().futureValue shouldBe Some(Set())
    }
  }
}
