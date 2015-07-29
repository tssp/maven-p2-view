package io.coding.me.m2p2.core.internal.model

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
 * Naive representation of a P2 artifact
 *
 * @author tim@coding-me.com
 */
trait P2Artifact extends Equals {

  def id: String
  def version: String
  def classifier: String
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

  class P2ArtifactImpl(val id: String, val version: String, val classifier: String) extends P2Artifact {

    import io.coding.me.m2p2.core.internal.extension.StringExtensions._

    require(classifier.isNotNullOrEmpty(), "Classifier of a P2 artifact must not be empty")
    require(id.isNotNullOrEmpty(), "Version of a P2 artifact must not be empty")
    require(version.isNotNullOrEmpty(), "Version of a P2 artifact must not be empty")

    override def canEqual(that: Any) = that.isInstanceOf[P2Artifact]

    override def equals(that: Any) = that match {

      case that: P2Artifact => that.canEqual(this) &&
        that.id == this.id &&
        that.version == this.version &&
        that.classifier == this.classifier

      case _ => false
    }
  }

  /**
   * Creates a list of P2 artifact representations based on a file, typically a p2artfiacts.xml file.
   */
  def apply(file: File): Try[List[P2Artifact]] = TryWithResource(new FileInputStream(file)).map { inputStream =>

    val p2artifacts = MutableList.empty[P2Artifact]

    val factory = XMLInputFactory.newInstance()
    val r = factory.createXMLEventReader(inputStream)

    var id: Option[String] = None
    var version: Option[String] = None
    var classifier: Option[String] = None

    while (r.hasNext()) {

      val event = r.nextEvent()

      event match {

        case s: StartElement if s.getName.getLocalPart == "artifact" =>
          val attributes = s.getAttributes.toList.asInstanceOf[List[Attribute]]

          id = attributes.find(_.getName.getLocalPart == "id").map(_.getValue).headOption
          version = attributes.find(_.getName.getLocalPart == "version").map(_.getValue).headOption
          classifier = attributes.find(_.getName.getLocalPart == "classifier").map(_.getValue).headOption

        case s: EndElement if s.getName.getLocalPart == "artifact" =>

          if (id.isDefined && version.isDefined && classifier.isDefined) {

            p2artifacts += new P2ArtifactImpl(id.get, version.get, classifier.get)

          } else {

            logger.warn("Artifact file ${file} seems to be invalid. Can't parse XML properly.")
          }

          id = None
          version = None
          classifier = None

        case _ => // noop
      }

    }

    logger.debug(s"Found ${p2artifacts.size} artifacts in file ${file}")

    p2artifacts.toList
  }

}