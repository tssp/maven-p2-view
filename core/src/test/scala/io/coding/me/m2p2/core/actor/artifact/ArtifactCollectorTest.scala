package io.coding.me.m2p2.core.actor.artifact

import java.io.File
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.testkit.DefaultTimeout
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import akka.testkit.TestActorRef
import io.coding.me.m2p2.core.MavenFile
import kamon.Kamon
import io.coding.me.m2p2.core.actor.RepositoryId
import scala.concurrent.duration._
import scala.concurrent.Await

/**
 *
 */
class ArtifactCollectorTest extends TestKit(ActorSystem("TestKitUsageSpec"))
    with DefaultTimeout with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll {

  import ArtifactCollector._

  def getRepositoryFile(name: String): MavenFile = new MavenFile(getClass.getResource(s"/reference_repository/${name}").toURI())

  override def beforeAll() = {

    Kamon.start()
  }

  override def afterAll() = {

    Kamon.shutdown()
  }

  val repositoryId = RepositoryId("repo")
  val collectorRef = TestActorRef(new ArtifactCollector(repositoryId))

  "An artifact collector" should {

    "properly restart its childs when an exception occurs" in {
      
      collectorRef.underlyingActor.insertArtifactRef ! new Exception("Testing supervisior strategy!")  
    }
    
    "properly select one of its childs" in {
    
      system.actorSelection("/user/*/insert/p2-artifact") ! new Exception("Testing analyzer execption supversivor strategy")
    }
    
    
    
    /*"escalate after too many exception" in {
      
      (0 to 20).foreach { i => collectorRef.underlyingActor.insertArtifactRef ! new Exception(s"Testing supervisior strategy (${i}. exception)!") }
    }*/
  }
}