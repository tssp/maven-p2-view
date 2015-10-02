package io.coding.me.m2p2.core.analyzer

import scala.util.{Try, Success, Failure}
import java.io.File


/**
 * example-bundle-2.0.0-20150528.145642-258-p2artifacts.xml
 */
trait FileNameVersionExtractor {
  
  val FilePattern = """(.+)-(\d.+-\d{8}\.\d{6}-\d+)-(p2artifacts.xml)""".r

  def extractMavenVersion(file: File): Try[String] = file.getName() match {

    case FilePattern(name, version, suffix) => Success(version)
    case _                                  => Failure(new Exception(s"File ${file} does not conform to P2 artifacts file name pattern. Can't extract substituted Maven version."))
  }
}