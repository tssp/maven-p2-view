package io.coding.me.m2p2.core.internal.model

import com.typesafe.scalalogging.LazyLogging
import java.io.File
import scala.util.Try
import io.coding.me.m2p2.core.internal.resource.TryWithResource
import java.io.FileInputStream
import scala.collection.mutable.MutableList
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.StartElement
import javax.xml.stream.events.EndElement

/**
 * See http://maven.apache.org/ref/3.2.5/maven-repository-metadata/repository-metadata.html
 */
object MavenMetadata extends LazyLogging {

  /**
   * Creates a list of Maven artifact representations based on a file, typically a maven-metadata.xml file.
   */
  def apply(file: File): Try[Set[MavenArtifact]] = TryWithResource(new FileInputStream(file)).map { inputStream =>

    val mavenArtifacts = MutableList.empty[MavenArtifact]

    val factory = XMLInputFactory.newInstance()
    val r = factory.createXMLEventReader(inputStream)

    var group: Option[String] = None
    var artifact: Option[String] = None

    var classifier: Option[String] = None
    var version: Option[String] = None
    var extension: Option[String] = None

    while (r.hasNext()) {

      val event = r.nextEvent()

      event match {

        case s: StartElement if s.getName.getLocalPart == "groupId" =>
          group = Some(r.getElementText())

        case s: StartElement if s.getName.getLocalPart == "artifactId" =>
          artifact = Some(r.getElementText())

        case s: StartElement if s.getName.getLocalPart == "classifier" =>
          classifier = Some(r.getElementText())

        case s: StartElement if s.getName.getLocalPart == "value" =>
          version = Some(r.getElementText())

        case s: StartElement if s.getName.getLocalPart == "extension" =>
          extension = Some(r.getElementText())

        case e: EndElement if e.getName.getLocalPart == "snapshotVersion" =>

          if (group.isDefined && artifact.isDefined && version.isDefined && extension.isDefined) {

            mavenArtifacts += MavenArtifact(group.get, artifact.get, version.get, extension.get, classifier)

          } else {

            logger.warn(s"File ${file} seems to be invalid. Please check it manually.")
          }

          classifier = None
          version = None
          extension = None

        case _ => // noop
      }

    }

    logger.debug(s"Found ${mavenArtifacts.size} different Maven versions in file ${file}")

    mavenArtifacts.toSet
  }
}