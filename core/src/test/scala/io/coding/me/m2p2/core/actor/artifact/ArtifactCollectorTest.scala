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
import io.coding.me.m2p2.core.actor.InsertArtifactRequest
import io.coding.me.m2p2.core.actor.InsertArtifactResponse
import io.coding.me.m2p2.core.actor.ActorStateRequest
import io.coding.me.m2p2.core.actor.artifact.InsertArtifactCollector.InsertArtifactStateResponse

/**
 *
 */
class ArtifactCollectorTest extends TestKit(ActorSystem("TestKitUsageSpec"))
    with DefaultTimeout with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll {

  import ArtifactCollector._

  def getRepositoryFile(name: String): MavenFile = new MavenFile(getClass.getResource(s"/reference_repository/example_v1/example/group/${name}").toURI())

  override def beforeAll() = {

    Kamon.start()
  }

  override def afterAll() = {

    Kamon.shutdown()
  }

  val repositoryId = RepositoryId("repo")
  val collectorRef = TestActorRef(new ArtifactCollector(repositoryId))

  val insertActor = system.actorSelection("/user/*/insert")
  val deleteActor = system.actorSelection("/user/*/delete")

  "An artifact collector" should {

    "properly react when inserting feature artifacts" in {

      val names = Array("-p2metadata.xml", "-p2artifacts.xml", "-root.cocoa.macosx.x86_64.zip", "-root.gtk.linux.x86_64.zip", "-root.win32.win32.x86_64.zip", "-root.zip", ".jar", ".pom").map(suffix => s"example-feature-0.1.0-SNAPSHOT${suffix}")
      val dir = "example-feature/0.1.0-SNAPSHOT"

      val files = names.map(name => getRepositoryFile(s"${dir}/${name}"))

      files.foreach { file =>

        collectorRef ! InsertArtifactRequest(repositoryId, file)

        val response = expectMsgType[InsertArtifactResponse]

        response.artifact shouldBe file
      }
      
      insertActor ! ActorStateRequest
     
      expectMsgPF() { 
        
        case r: InsertArtifactStateResponse => 
          r.state shouldBe "Yeehaw"
      }
    }
  }

  /*
  "An artifact collector" should {
   

    "properly restart its childs when an exception occurs" in {
      
      collectorRef.underlyingActor.insertArtifactRef ! new Exception("Testing supervisior strategy!")  
    }
    
    "properly select one of its childs" in {
    
      system.actorSelection("/user/*/insert/p2-artifact") ! new Exception("Testing analyzer execption supversivor strategy")
    }
  }
  * */
  */
}