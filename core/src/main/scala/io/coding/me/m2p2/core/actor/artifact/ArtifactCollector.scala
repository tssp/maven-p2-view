package io.coding.me.m2p2.core.actor.artifact

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import io.coding.me.m2p2.core.actor.RepositoryId
import io.coding.me.m2p2.core.actor.InsertArtifactRequest
import io.coding.me.m2p2.core.actor.DeleteArtifactRequest

/**
 * Companion object
 */
object ArtifactCollector {

  /**
   * Factory method for the actor system
   */
  def props(repositoryId: RepositoryId): Props = Props(new ArtifactCollector(repositoryId))

}

class ArtifactCollector(repositoryId: RepositoryId) extends Actor with ActorLogging {

  val insertArtifactRef= context.actorOf(InsertArtifactCollector.props(repositoryId), "insert")
  val deleteArtifactRef= context.actorOf(DeleteArtifactCollector.props(repositoryId), "delete")
  
  override def receive = {

    case iar: InsertArtifactRequest =>  
      insertArtifactRef.forward(iar)

    case dar: DeleteArtifactRequest =>  
      deleteArtifactRef.forward(dar)      
      
  }
}