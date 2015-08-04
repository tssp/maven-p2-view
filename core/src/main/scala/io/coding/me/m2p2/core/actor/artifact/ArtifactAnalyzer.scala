package io.coding.me.m2p2.core.actor.artifact

import java.io.File

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import ArtifactAnalyzer.AnalyzeRequest
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.actorRef2Scala
import io.coding.me.m2p2.core.actor.RepositoryId
import io.coding.me.m2p2.core.analyzer._
import io.coding.me.m2p2.core.internal.metric.FileAnalyzerMetrics
import io.coding.me.m2p2.core.internal.metric.convert2extension


/**
 * Companion object
 */
object ArtifactAnalyzer {

  case class AnalyzeRequest(file: File)

  trait AnalyzeResponse[T] {

    def file: File
    def result: Option[T]
  }

  case class P2MetadataAnalyzeResponse(val file: File, val result: Option[Set[P2Unit]]) extends AnalyzeResponse[Set[P2Unit]]
  case class P2ArtifactAnalyzeResponse(val file: File, val result: Option[Set[P2Artifact]]) extends AnalyzeResponse[Set[P2Artifact]]
  case class P2FeatureAnalyzeResponse(val file: File, val result: Option[P2Feature]) extends AnalyzeResponse[P2Feature]

  /**
   * Factory method for the actor system
   */
  def props[T](repositoryId: RepositoryId, name: String, analyzer: File => Try[Option[T]], factory: (File, Option[T]) => AnalyzeResponse[T]): Props = Props(new ArtifactAnalyzer(repositoryId, name, analyzer, factory))

  /**
   * Analyzer for JAR files containing feature.xml files
   */
  def p2featureJarProps(repositoryId: RepositoryId) = props[P2Feature](repositoryId, "jar-file", P2Feature.apply, (file, result) => P2FeatureAnalyzeResponse(file, result))
  
  /**
   * Analyzer for *.p2metadata.xml files containing P2Unit artifacts
   */
  def p2metadataProps(repositoryId: RepositoryId) = props[Set[P2Unit]](repositoryId, "p2metadata-file", P2Metadata.apply, (file, result) => P2MetadataAnalyzeResponse(file, result))

  /**
   * Analyzer for *.p2artifacts.xml files containing P2Artifact artifacts
   */
  def p2artifactProps(repositoryId: RepositoryId) = props[Set[P2Artifact]](repositoryId, "p2artifact-file", P2Artifact.apply, (file, result) => P2ArtifactAnalyzeResponse(file, result))

  
}

class ArtifactAnalyzer[T](repositoryId: RepositoryId, name: String, analyzer: File => Try[Option[T]], factory: (File, Option[T]) => ArtifactAnalyzer.AnalyzeResponse[T]) extends Actor with ActorLogging {

  require(analyzer != null, "Artifact analyzer must not be null")
  require(factory != null, "Message factory must not be null")
  
  import io.coding.me.m2p2.core.internal.metric._
  import ArtifactAnalyzer._

  val metrics = FileAnalyzerMetrics(name)

  override def receive = {

    case AnalyzeRequest(file) =>

      metrics.files.increment()
      metrics.processingTime.recordTime(analyzer(file)) match {

        case Success(result) =>
          sender ! factory(file, result)

        case Failure(ex) =>
          log.error(ex, s"Could not analyze file ${file}, ignoring artifact.")
          metrics.errors.increment()
      }
  }
}