package io.coding.me.m2p2.core.actor

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import org.joda.time.DateTime

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

  override def postStop() = {

    log.info(s"Stopping repository-receptionist ${repositoryId} (what a pity :-( )")
  }

  override def receive = {

    case _ => // noop
  }
}