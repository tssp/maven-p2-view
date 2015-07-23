package io.coding.me.m2p2.core

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
   * Request to create a new repository
   */
  case class CreateRepositoryRequest(id: RepositoryId)
  
  /**
   * Response
   */
  case class CreateRepositoryResponse(id: RepositoryId, existing: Boolean)
  
  
  /**
   * Request to delete a repository
   */
  case class DeleteRepositoryRequest(id: RepositoryId)
  
  /**
   * Response
   */
  case class DeleteRepositoryResponse(id: RepositoryId, deleted: Boolean, reason: Option[String])  
}