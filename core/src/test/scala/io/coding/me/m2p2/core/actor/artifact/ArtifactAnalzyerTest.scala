package io.coding.me.m2p2.core.actor.artifact

import java.io.File
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.testkit.DefaultTimeout
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import io.coding.me.m2p2.core.MavenFile
import io.coding.me.m2p2.core.actor.RepositoryId
import io.coding.me.m2p2.core.actor.artifact.ArtifactAnalyzer._
import kamon.Kamon

abstract class ArtifactAnalyzerTest[T] extends TestKit(ActorSystem("TestKitUsageSpec"))
    with DefaultTimeout with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def beforeAll() = {

    Kamon.start()
  }

  override def afterAll() = {

    Kamon.shutdown()
  }

  def getResourceFile(name: String): MavenFile = new MavenFile(getClass.getResource(s"/reference_files/${name}").toURI())

  val repositoryId = RepositoryId("repo")

  def analyzerActor: ActorRef
  def analyzerFile: File

  "An analyzer actor" should {

    "not respond when providing an invalid file" in {

      analyzerActor ! AnalyzeRequest(new File("xxx"))
      expectNoMsg()
    }

    "respond with a proper result when providing a valid file" in {

      analyzerActor ! AnalyzeRequest(analyzerFile)
      val response = expectMsgAnyClassOf(classOf[AnalyzeResponse[T]])

      response should not be null
      response.file shouldBe analyzerFile
      response.result should not be empty
    }
  }
}