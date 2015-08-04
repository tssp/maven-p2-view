package io.coding.me.m2p2.core.analyzer

import java.io.File
import java.io.FileInputStream
import scala.collection.JavaConversions.asScalaIterator
import scala.collection.mutable.MutableList
import scala.util.Try
import com.typesafe.scalalogging.LazyLogging
import io.coding.me.m2p2.core.internal.extension.StringExtensions.string2extension
import io.coding.me.m2p2.core.internal.resource.TryWithResource
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement

/**
 * Naive representation of a P2 installable unit
 */
case class P2Artifact(id: String, version: String, classifier: Option[String], mavenReference: MavenGAV) {

  require(id.isNotNullOrEmpty(), "Version of a P2 artifact must not be empty")
  require(version.isNotNullOrEmpty(), "Version of a P2 artifact must not be empty")
}

/**
 * Typical format:
 * {{{
 * <?xml version='1.0' encoding='UTF-8'?>
 * <?artifactRepository version='1.1.0'?>
 * <artifacts size='1'>
 *   <artifact classifier='osgi.bundle' id='example-bundle' version='0.1.0.201507201658'>
 *     <properties size='5'>
 *       <property name='artifact.size' value='61424'/>
 *       <property name='download.size' value='61424'/>
 *       <property name='maven-groupId' value='example.group'/>
 *       <property name='maven-artifactId' value='example-bundle'/>
 *       <property name='maven-version' value='0.1.0-SNAPSHOT'/>
 *     </properties>
 *   </artifact>
 * </artifacts>
 * }}}
 */
object P2Artifact extends LazyLogging {

  import io.coding.me.m2p2.core.internal.extension.StringExtensions._

  /**
   * Creates a list of P2 artifact representations based on a file, typically a p2artfiacts.xml file.
   */
  def apply(file: File): Try[Option[Set[P2Artifact]]] = TryWithResource(new FileInputStream(file)).map { inputStream =>

    val p2artifacts = MutableList.empty[P2Artifact]

    val factory = XMLInputFactory.newInstance()
    val r = factory.createXMLEventReader(inputStream)

    var id: Option[String] = None
    var version: Option[String] = None
    var classifier: Option[String] = None

    var properties: Map[String, String] = Map.empty

    while (r.hasNext()) {

      val event = r.nextEvent()

      event match {

        case s: StartElement if s.getName.getLocalPart == "property" =>
          val attributes = s.getAttributes.toList.asInstanceOf[List[Attribute]]

          properties ++= attributes.map { attribute =>

            val name = attributes.find(_.getName.getLocalPart == "name").map(_.getValue).headOption
            val value = attributes.find(_.getName.getLocalPart == "value").map(_.getValue).headOption

            name -> value

          }.filter(kv => kv._1.isDefined && kv._2.isDefined).map(kv => kv._1.get -> kv._2.get).toMap

        case s: StartElement if s.getName.getLocalPart == "artifact" =>
          val attributes = s.getAttributes.toList.asInstanceOf[List[Attribute]]

          id = attributes.find(_.getName.getLocalPart == "id").map(_.getValue).headOption
          version = attributes.find(_.getName.getLocalPart == "version").map(_.getValue).headOption
          classifier = attributes.find(_.getName.getLocalPart == "classifier").map(_.getValue).headOption

        case s: EndElement if s.getName.getLocalPart == "artifact" =>

          val mavenGroupId = properties.get("maven-groupId")
          val mavenArtifactId = properties.get("maven-artifactId")
          val mavenVersion = properties.get("maven-version")
          val mavenClassifier = properties.get("maven-classifier")
          val mavenExtension = properties.get("maven-extension")

          if (id.isDefined && version.isDefined && classifier.isDefined &&
            mavenGroupId.isDefined && mavenArtifactId.isDefined && mavenVersion.isDefined) {

            val mavenGav = MavenGAV(mavenGroupId.get, mavenArtifactId.get, mavenVersion.get, mavenClassifier, mavenExtension)
            val p2artifact = P2Artifact(id.get, version.get, classifier, mavenGav)

            p2artifacts += p2artifact

          } else {

            logger.warn(s"Artifact file ${file} seems to be invalid. Can't parse XML properly.")
          }

          id = None
          version = None
          classifier = None

          properties = Map.empty

        case _ => // noop
      }

    }

    logger.debug(s"Found ${p2artifacts.size} artifacts in file ${file}")

    Some(p2artifacts.toSet)
  }
}