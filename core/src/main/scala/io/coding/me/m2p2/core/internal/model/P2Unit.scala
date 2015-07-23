package io.coding.me.m2p2.core.internal.model

import java.io.File
import java.io.FileInputStream

import scala.collection.JavaConversions.asScalaIterator
import scala.collection.mutable.MutableList
import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import io.coding.me.m2p2.core.internal.extension.StringExtensions.isNotNullOrEmpty
import io.coding.me.m2p2.core.internal.resource.TryWithResource
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement

/**
 * Naive representation of a P2 installable unit
 *
 * @author tim@coding-me.com
 */
case class P2Unit(id: String, version: String, artifacts: List[P2Unit.Artifact]) {

  require(id.isNotNullOrEmpty(), "Version of a P2 unit must not be empty")
  require(version.isNotNullOrEmpty(), "Version of a P2 unit must not be empty")
}

/**
 * Typical format:
 * {{{
 * <?xml version='1.0' encoding='UTF-8'?>
 * <units size='1'>
 *   <unit id='example-bundle' version='0.1.0.201507201658'>
 *     <update id='example-bundle' range='[0.0.0,0.1.0.201507201658)' severity='0'/>
 *     <properties size='5'>
 *       <property name='org.eclipse.equinox.p2.name' value='Bundle'/>
 *       <property name='org.eclipse.equinox.p2.provider' value='Eclipse.org'/>
 *       <property name='maven-groupId' value='example.group'/>
 *       <property name='maven-artifactId' value='example-bundle'/>
 *       <property name='maven-version' value='0.1.0-SNAPSHOT'/>
 *     </properties>
 *     <provides size='3'>
 *       <provided namespace='org.eclipse.equinox.p2.iu' name='example-bundle' version='0.1.0.201507201658'/>
 *       <provided namespace='osgi.bundle' name='example-bundle' version='0.1.0.201507201658'/>
 *       <provided namespace='org.eclipse.equinox.p2.eclipse.type' name='bundle' version='1.0.0'/>
 *     </provides>
 *     <requires size='3'>
 *       <required namespace='osgi.bundle' name='org.eclipse.core.runtime' range='0.0.0'/>
 *       <required namespace='osgi.bundle' name='org.eclipse.swt' range='3.6.0'/>
 *       <required namespace='osgi.bundle' name='org.eclipse.ui' range='3.6.0'/>
 *     </requires>
 *     <artifacts size='1'>
 *       <artifact classifier='osgi.bundle' id='example-bundle' version='0.1.0.201507201658'/>
 *     </artifacts>
 *     <touchpoint id='org.eclipse.equinox.p2.osgi' version='1.0.0'/>
 *     <touchpointData size='1'>
 *       <instructions size='1'>
 *         <instruction key='manifest'>
 *           Bundle-SymbolicName: example-bundle;singleton:=true&#xA;Bundle-Version: 0.1.0.201507201658&#xA;
 *         </instruction>
 *       </instructions>
 *     </touchpointData>
 *   </unit>
 * </units>
 * }}}
 */
object P2Unit extends LazyLogging {

  case class Artifact(id: String, version: String, classifier: String) {

    require(classifier.isNotNullOrEmpty(), "Classifier of a P2 unit artifact must not be empty")
    require(id.isNotNullOrEmpty(), "Version of a P2 unit artifact must not be empty")
    require(version.isNotNullOrEmpty(), "Version of a P2 unit artifact must not be empty")
  }

  /**
   * Creates a list of P2 unit representations based on a file, typically a p2content.xml file.
   */
  def apply(file: File): Try[List[P2Unit]] = TryWithResource(new FileInputStream(file)).map { inputStream =>

    val p2units = MutableList.empty[P2Unit]

    val factory = XMLInputFactory.newInstance()
    val r = factory.createXMLEventReader(inputStream)

    var id: Option[String] = None
    var version: Option[String] = None

    val artifacts = MutableList.empty[Artifact]

    while (r.hasNext()) {

      val event = r.nextEvent()

      event match {

        case s: StartElement if s.getName.getLocalPart == "artifact" =>
          val attributes = s.getAttributes.toList.asInstanceOf[List[Attribute]]

          val a_id = attributes.find(_.getName.getLocalPart == "id").map(_.getValue).headOption
          val a_version = attributes.find(_.getName.getLocalPart == "version").map(_.getValue).headOption
          val a_classifier = attributes.find(_.getName.getLocalPart == "classifier").map(_.getValue).headOption
          
          if (a_id.isDefined && a_version.isDefined && a_classifier.isDefined) {
            
            artifacts += Artifact(a_id.get, a_version.get, a_classifier.get)
            
          } else {
            
            logger.warn(s"Content file ${file} seems to have invalid artifact sections. Can't parse XML properly.")
          }
        
        case s: StartElement if s.getName.getLocalPart == "unit" =>
          val attributes = s.getAttributes.toList.asInstanceOf[List[Attribute]]

          id = attributes.find(_.getName.getLocalPart == "id").map(_.getValue).headOption
          version = attributes.find(_.getName.getLocalPart == "version").map(_.getValue).headOption

        case s: EndElement if s.getName.getLocalPart == "unit" =>

          if (id.isDefined && version.isDefined) {

            p2units += P2Unit(id.get, version.get, artifacts.toList)

          } else {

            logger.warn(s"Content file ${file} seems to be invalid. Can't parse XML properly.")
          }

          id = None
          version = None
          artifacts.clear()

        case _ => // noop
      }
    }
    
    logger.debug(s"Found ${p2units.size} units and ${p2units.flatMap(_.artifacts).size} artifacts in file ${file}")

    p2units.toList
  }

}