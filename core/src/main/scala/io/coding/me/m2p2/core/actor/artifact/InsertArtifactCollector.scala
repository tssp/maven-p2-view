package io.coding.me.m2p2.core.actor.artifact

import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.OneForOneStrategy
import akka.actor.Props
import akka.actor.SupervisorStrategy.Restart
import akka.actor.actorRef2Scala
import io.coding.me.m2p2.core.MavenFile
import io.coding.me.m2p2.core.actor.InsertArtifactRequest
import io.coding.me.m2p2.core.actor.InsertArtifactResponse
import io.coding.me.m2p2.core.actor.RepositoryId
import io.coding.me.m2p2.core.internal.metric.ArtifactCollectorMetrics
import io.coding.me.m2p2.core.actor._


/**
 * Companion object
 */
object InsertArtifactCollector {

  case class InsertArtifactStateResponse(state: String) extends ActorStateResponse[String]
  
  /**
   * Factory method for the actor system
   */
  def props(repositoryId: RepositoryId): Props = Props(new InsertArtifactCollector(repositoryId))

}

class InsertArtifactCollector(repositoryId: RepositoryId) extends Actor with ActorLogging {
 
  import InsertArtifactCollector._
  
  def triggersUpdate(mavenFile: MavenFile): Boolean = false

  log.info(s"Initalizing insert artifact collector for repository ${repositoryId.id}")
  
  val analyzerP2Artifact = context.actorOf(ArtifactAnalyzer.p2artifactProps(repositoryId), "p2-artifact")
  val analyzerP2Metadata = context.actorOf(ArtifactAnalyzer.p2metadataProps(repositoryId), "p2-metadata")
  val analyzerP2Feature = context.actorOf(ArtifactAnalyzer.p2featureJarProps(repositoryId), "p2-feature")

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
      
    case ActorStateRequest =>
      sender ! InsertArtifactStateResponse("Yeehaw")

    case iar: InsertArtifactRequest =>

      if (triggersUpdate(iar.artifact))
        sender ! InsertArtifactResponse(iar.id, iar.artifact, true)
      else
        sender ! InsertArtifactResponse(iar.id, iar.artifact, false)

  }
}