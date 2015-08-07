package io.coding.me.m2p2.core.actor.artifact

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import io.coding.me.m2p2.core.actor.DeleteArtifactRequest
import io.coding.me.m2p2.core.actor.InsertArtifactRequest
import io.coding.me.m2p2.core.actor.RepositoryId
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import scala.concurrent.duration._

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

  log.info(s"Initalizing artifact collector for repository ${repositoryId.id}")

  val insertArtifactRef = context.actorOf(InsertArtifactCollector.props(repositoryId), "insert")
  val deleteArtifactRef = context.actorOf(DeleteArtifactCollector.props(repositoryId), "delete")
/*
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute, loggingEnabled= true) {
    case _: Exception => Resume
  }*/

  override def receive = {

    case ex: Exception =>
      log.error("Argh, someone send me an exception. This should only happen for testing purposes!")
      throw ex

    case iar: InsertArtifactRequest =>
      insertArtifactRef.forward(iar)

    case dar: DeleteArtifactRequest =>
      deleteArtifactRef.forward(dar)

  }
}