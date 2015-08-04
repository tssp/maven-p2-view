package io.coding.me.m2p2.core.analyzer

import java.io.File
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile

import scala.collection.JavaConversions.asScalaIterator
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import io.coding.me.m2p2.core.internal.resource.TryWithResource
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.StartElement

object P2Feature extends LazyLogging {

  import io.coding.me.m2p2.core.internal.extension.StringExtensions._
  import scala.collection.JavaConversions._

  /**
   * Naive representation of an P2 plugin that is referenced by a P2 feature
   */
  case class Plugin(id: String, version: String)

  /**
   * Naive representation of an P2 feature
   */
  case class P2Feature(id: String, version: String, plugins: Set[Plugin])

  /**
   * Creates a feature representations based on a jar file
   */
  def apply(file: File): Try[Option[P2Feature]] = TryWithResource(new JarFile(file)).map { jarFile =>

    Option[JarEntry](jarFile.getJarEntry("/feature.xml")).flatMap { jarEntry =>

      TryWithResource(jarFile.getInputStream(jarEntry)).flatMap { is =>  apply(is) } match {

        case Success(p2feature) =>
          p2feature

        case Failure(ex) =>
          logger.error(s"Could not read feature.xml in ${file}", ex)
          None

      }
    }
  }

  def apply(inputStream: InputStream): Try[Option[P2Feature]] = Try {

    val factory = XMLInputFactory.newInstance()
    val r = factory.createXMLEventReader(inputStream)

    var id: Option[String] = None
    var version: Option[String] = None

    var pluginId: Option[String] = None
    var pluginVersion: Option[String] = None

    var pluginSet = Set.empty[Plugin]

    while (r.hasNext()) {

      val event = r.nextEvent()

      event match {

        case s: StartElement if s.getName.getLocalPart == "feature" =>

          val attributes = s.getAttributes.toList.asInstanceOf[List[Attribute]]

          id = attributes.find(_.getName.getLocalPart == "id").map(_.getValue).headOption
          version = attributes.find(_.getName.getLocalPart == "version").map(_.getValue).headOption

        case s: StartElement if s.getName.getLocalPart == "plugin" =>

          val attributes = s.getAttributes.toList.asInstanceOf[List[Attribute]]

          pluginId = attributes.find(_.getName.getLocalPart == "id").map(_.getValue).headOption
          pluginVersion = attributes.find(_.getName.getLocalPart == "version").map(_.getValue).headOption

          if (pluginId.isDefined && pluginVersion.isDefined) {

            pluginSet += Plugin(pluginId.get, pluginVersion.get)

          } else {

            logger.warn(s"Feature.xml seems to be invalid. Id or version in plugin section not found")
          }

        case _ => // noop
      }

    }

    if (id.isDefined && version.isDefined) {

      Some(P2Feature(id.get, version.get, pluginSet.toSet))

    } else {

      logger.warn(s"Feature.xml seems to be invalid. Id or version in feature section not found")
      None
    }
  }
}