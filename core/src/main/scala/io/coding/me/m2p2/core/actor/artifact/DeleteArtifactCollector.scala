package io.coding.me.m2p2.core.actor.artifact

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import io.coding.me.m2p2.core.actor.RepositoryId

/**
 * Companion object
 */
object DeleteArtifactCollector {

  /**
   * Factory method for the actor system
   */
  def props(repositoryId: RepositoryId): Props = Props(new DeleteArtifactCollector(repositoryId))

}

class DeleteArtifactCollector(repositoryId: RepositoryId) extends Actor with ActorLogging {


  override def receive = {

    case _ => // noop      
      
  }
}