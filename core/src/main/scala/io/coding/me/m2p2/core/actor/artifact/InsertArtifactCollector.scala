package io.coding.me.m2p2.core.actor.artifact

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.actorRef2Scala
import io.coding.me.m2p2.core.MavenFile
import io.coding.me.m2p2.core.actor.InsertArtifactRequest
import io.coding.me.m2p2.core.actor.InsertArtifactResponse
import io.coding.me.m2p2.core.actor.RepositoryId

/**
 * Companion object
 */
object InsertArtifactCollector {

  /**
   * Factory method for the actor system
   */
  def props(repositoryId: RepositoryId): Props = Props(new InsertArtifactCollector(repositoryId))

}

class InsertArtifactCollector(repositoryId: RepositoryId) extends Actor with ActorLogging {

  def triggersUpdate(mavenFile: MavenFile): Boolean = false

  override def receive = {

    case iar: InsertArtifactRequest =>

      if (triggersUpdate(iar.artifact))
        sender ! InsertArtifactResponse(iar.id, iar.artifact, true)
      else
        sender ! InsertArtifactResponse(iar.id, iar.artifact, false)
  }
}