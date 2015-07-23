package io.coding.me.m2p2.core.actor

import io.coding.me.m2p2.core.actor.RepositoryRouter._;

import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpecLike
import org.scalatest.Matchers

import akka.actor.ActorSystem
import akka.testkit.{ DefaultTimeout, ImplicitSender, TestKit }
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

/**
 * 
 */
class RepositoryRouterTest extends TestKit(ActorSystem("TestKitUsageSpec"))
    with DefaultTimeout with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll {


  import RepositoryRouter._
  
  val routerRef = system.actorOf(RepositoryRouter.props())
  
  "A repository router" should {
    
    "respond with an empty list of repositories when asked for" in {
      
      routerRef ! ListRepositoriesRequest
      expectMsg(ListRepositoriesResponse(Set[RepositoryId]()))
    }
    
    val id= RepositoryId("repo-1")
    
    /*"create a new repository receptionist when asked for" in {
      
      routerRef ! CreateRepositoryRequest(id)
      expectMsg(CreateRepositoryResponse(id, true))
    }
    
    "not create a new repository receptionist when repository already exists" in {
      
      routerRef ! CreateRepositoryRequest(id)
      expectMsg(CreateRepositoryResponse(id, false))
    }
    
    "respond with a non-empty list of repositories when asked for" in {
      
      routerRef ! ListRepositoriesRequest
      
      val r= expectMsgType[ListRepositoriesResponse]
      
      assert(r.repositories.size == 1)
      assert(r.repositories.head.id == id)
    }*/
  }
}