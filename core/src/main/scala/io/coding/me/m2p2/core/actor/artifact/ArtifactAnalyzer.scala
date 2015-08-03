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
import io.coding.me.m2p2.core.analyzer.P2Feature
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

  case class JarAnalyzeResponse(val file: File, val result: Option[P2Feature]) extends AnalyzeResponse[P2Feature]

  /**
   * Factory method for the actor system
   */
  def props[T](repositoryId: RepositoryId, name: String, analyzer: File => Try[Option[T]], factory: (File, Option[T]) => AnalyzeResponse[T]): Props = Props(new ArtifactAnalyzer(repositoryId, name, analyzer, factory))

  def jarProps(repositoryId: RepositoryId) = props[P2Feature](repositoryId, "jar-file", null, (file, result) => JarAnalyzeResponse(file, result))

}

class ArtifactAnalyzer[T](repositoryId: RepositoryId, name: String, analyzer: File => Try[Option[T]], factory: (File, Option[T]) => ArtifactAnalyzer.AnalyzeResponse[T]) extends Actor with ActorLogging {

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
          log.info(s"Could not analyze file ${file}, ignoring artifact.", ex)
          metrics.errors.increment()
      }
  }
}