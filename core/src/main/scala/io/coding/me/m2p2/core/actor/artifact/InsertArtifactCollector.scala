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
import io.coding.me.m2p2.core.MavenFile
import akka.actor.ActorRef
import io.coding.me.m2p2.core.actor.artifact.ArtifactAnalyzer._
import io.coding.me.m2p2.core.actor.ActorStateResponse
 
/**
 * Companion object
 */
object InsertArtifactCollector {

  val P2MetadataFilePattern = """.*-p2metadata.xml$""".r
  val P2ArtifactsFilePattern = """.*-p2artifacts.xml$""".r
  val JarFilePattern = """.*.jar$""".r

  
  /**
   * Factory method for the actor system
   */
  def props(repositoryId: RepositoryId): Props = Props(new InsertArtifactCollector(repositoryId))

}

class InsertArtifactCollector(repositoryId: RepositoryId) extends Actor with ActorLogging {

  import InsertArtifactCollector._

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


  def findCorrespondingAnalyzer(artifact: MavenFile): Option[ActorRef] = artifact.getName match {

    case P2MetadataFilePattern()  => Some(analyzerP2Metadata)
    case P2ArtifactsFilePattern() => Some(analyzerP2Artifact)
    case JarFilePattern()         => Some(analyzerP2Feature)
    
    case _                        => None
  }

  override def receive = {

    case ex: Exception =>
      log.error("Argh, someone send me an exception. This should only happen for testing purposes!")
      throw ex

    case ActorStateRequest => sender ! ActorStateResponse("Yeehaw")

    case InsertArtifactRequest(id, artifact) =>

      findCorrespondingAnalyzer(artifact) match {
        
        case Some(ref) => 
          ref ! AnalyzeRequest(artifact)
          sender ! InsertArtifactResponse(id, artifact, true) // inform parent that file is considered
        
        case None => 
          log.debug("Could not find a corresponding analyzer for {}. Omitting this file. ", artifact)
          sender ! InsertArtifactResponse(id, artifact, false) // inform parent that file is not considered
      }
      
    case response: P2MetadataAnalyzeResponse =>
      
  }
}