package io.coding.me.m2p2

import java.io.File

package object core {

  type P2File = File
  type MavenFile = File

  /**
   * Some statistical information about the repositories
   */
  case class RepositoryStatistic(name: String)
}