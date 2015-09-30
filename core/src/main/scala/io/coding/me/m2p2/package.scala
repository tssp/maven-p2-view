package io.coding.me

package object m2p2 {

  import java.io.File

  type Artifacts = Seq[Artifact]
  type InstallableUnits = Seq[InstallableUnit]

  case class Content(artifacts: Artifacts, installableUnits: InstallableUnits)

  case class VirtualView(name: String, metaFiles: MetaData, content: Content)

  case class MetaDataFile(xml: File, jar: File)
  case class MetaData(p2Index: File, contentFile: MetaDataFile, artifactFile: MetaDataFile)

  case class Artifact(name: String, version: String, classifier: Option[String])
  case class InstallableUnit(name: String, version: String)
}