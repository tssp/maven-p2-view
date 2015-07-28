package io.coding.me.m2p2.core.actor

import java.io.File
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.testkit.DefaultTimeout
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import io.coding.me.m2p2.core.MavenFile
import kamon.Kamon

/**
 *
 */
class RepositoryRouterTest extends TestKit(ActorSystem("TestKitUsageSpec"))
    with DefaultTimeout with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll {

  import RepositoryRouter._

  def getRepositoryFile(name: String): MavenFile = new MavenFile(getClass.getResource(s"/reference_repository/${name}").toURI())

  override def beforeAll() = {

    Kamon.start()
  }

  override def afterAll() = {

    Kamon.shutdown()
  }

  val routerRef = system.actorOf(RepositoryRouter.props())

  "A repository router" should {

    "respond with an empty list of repositories when asked for" in {

      routerRef ! ListRepositoriesRequest
      expectMsg(ListRepositoriesResponse(Set[RepositoryId]()))
    }

    val id = RepositoryId("repo-1")

    "create a new repository receptionist when asked for" in {

      routerRef ! CreateRepositoryRequest(id)
      expectMsg(CreateRepositoryResponse(id, false))
    }

    "not create a new repository receptionist when repository already exists" in {

      routerRef ! CreateRepositoryRequest(id)
      expectMsg(CreateRepositoryResponse(id, true))
    }

    "respond with a non-empty list of repositories when asked for" in {

      routerRef ! ListRepositoriesRequest

      val r = expectMsgType[ListRepositoriesResponse]

      r.repositories.size shouldBe 1
      r.repositories.head shouldBe id
    }

    "respond properly when inserting an artifact" in {

      val file = new File(".") //getRepositoryFile("xxx"))

      routerRef ! InsertArtifactRequest(id, file)

      val r = expectMsgType[InsertArtifactResponse]

      r shouldBe InsertArtifactResponse(id, file, false)
    }

    "remove a repository when asked for" in {

      routerRef ! DeleteRepositoryRequest(id)

      val r = expectMsgType[DeleteRepositoryResponse]

      r.id shouldBe id
      r.deleted shouldBe true
    }

    "return an error when trying to remove a repository that does not exist" in {

      routerRef ! DeleteRepositoryRequest(id)

      val r = expectMsgType[DeleteRepositoryResponse]

      r.id shouldBe id
      r.deleted shouldBe false
      r.reason shouldBe Some("Repository does not exist")
    }

    "respond with an empty list of repositories after deletion" in {

      routerRef ! ListRepositoriesRequest

      val r = expectMsgType[ListRepositoriesResponse]

      r.repositories.size shouldBe 0
    }
  }
}