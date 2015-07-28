package io.coding.me.m2p2.api

import io.coding.me.m2p2.core._

import scala.concurrent.Future
import java.io.File

trait Repository {

  def insert(mavenArtifact: MavenFile): Future[Unit]
  def removeA(mavenArtifact: MavenFile): Future[Unit]

  def getArtifact(p2Artifact: P2File): Future[Option[MavenFile]]

  def getStatistic(): Future[RepositoryStatistic]
}