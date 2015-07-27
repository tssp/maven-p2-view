package io.coding.me.m2p2.core.actor

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import org.joda.time.DateTime
import io.coding.me.m2p2.core.actor.artifact.ArtifactCollector

/**
 * Companion object
 */
object RepositoryReceptionist {

  /**
   * Factory method for the actor system
   */
  def props(repositoryId: RepositoryId): Props = Props(new RepositoryReceptionist(repositoryId, DateTime.now()))

}

class RepositoryReceptionist(repositoryId: RepositoryId, created: DateTime) extends Actor with ActorLogging {

  log.info(s"Creating repository-receptionist ${repositoryId}")

  val artifactCollectorRef = context.actorOf(ArtifactCollector.props(repositoryId), "actor-collector")

  override def receive = {

    case iar: InsertArtifactRequest => 
      artifactCollectorRef.forward(iar)
      
    case dar: DeleteArtifactRequest => 
      artifactCollectorRef.forward(dar)
  }
}