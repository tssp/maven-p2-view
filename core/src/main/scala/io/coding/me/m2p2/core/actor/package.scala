package io.coding.me.m2p2.core

import io.coding.me.m2p2.core.internal.model.MavenArtifact
package object actor {

  // Messages

  /**
   * Repository Identifier, convenience class to avoid illegal repository ids
   */
  case class RepositoryId(id: String) {
    require(id != null && !id.isEmpty())
  }

  /**
   * Provides basic information about a repository
   */
  case class RepositoryInfo(id: RepositoryId, created: Long)

  /**
   * Requests for the available repositories
   */
  case object ListRepositoriesRequest

  /**
   * Response for the available repositories
   */
  case class ListRepositoriesResponse(repositories: Set[RepositoryId])

  /**
   * Base trait for all repository related requests
   */
  trait RepositoryRequest {
    
    def id: RepositoryId
  }
  
  /**
   * Request to create a new repository
   */
  case class CreateRepositoryRequest(id: RepositoryId) extends RepositoryRequest

  /**
   * Response
   */
  case class CreateRepositoryResponse(id: RepositoryId, existing: Boolean)

  /**
   * Request to delete a repository
   */
  case class DeleteRepositoryRequest(id: RepositoryId) extends RepositoryRequest

  /**
   * Response
   */
  case class DeleteRepositoryResponse(id: RepositoryId, deleted: Boolean, reason: Option[String])

  /**
   * Request to insert a new file into the view
   */
  case class InsertArtifactRequest(id: RepositoryId, artifact: MavenFile) extends RepositoryRequest

  /**
   * Response
   */
  case class InsertArtifactResponse(id: RepositoryId, artifact: MavenFile, updateTriggered: Boolean) extends RepositoryRequest
  
    /**
   * Request to delete a file from the view
   */
  case class DeleteArtifactRequest(id: RepositoryId, artifact: MavenFile) extends RepositoryRequest

  /**
   * Response
   */
  case class DeleteArtifactResponse(id: RepositoryId, artifact: MavenFile, updateTriggered: Boolean) extends RepositoryRequest
  
}