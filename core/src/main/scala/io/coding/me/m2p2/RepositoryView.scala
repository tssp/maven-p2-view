package io.coding.me.m2p2

import java.io.File

import scala.concurrent.Future

import io.coding.me.m2p2.core.MavenFile
import io.coding.me.m2p2.core.P2File

trait RepositoryView {
  
  def insertArtifact(mavenArtifact: MavenFile, updateView: Option[VirtualView => Unit]): Future[Unit]
  def removeArtifact(mavenArtifact: MavenFile, updateView: Option[VirtualView => Unit]): Future[Unit]
  
  def getArtifact(p2Artifact: P2File): Future[Option[MavenFile]]

  def getVirtualView(): Future[Option[VirtualView]]
}

