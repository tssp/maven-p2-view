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

  log.info(s"Initalizing delete artifact collector for repository ${repositoryId.id}")

  /**
   * Restarting this actor might lead to data loss. TODO: This must be handled properly!
   */
  override def preRestart(reason: Throwable, message: Option[Any]) = {
    
    super.preRestart(reason, message)
  }  
  
  override def receive = {

    case ex: Exception =>
      log.error("Argh, someone send me an exception. This should only happen for testing purposes!")
      throw ex

    case _ => // noop      

  }
}