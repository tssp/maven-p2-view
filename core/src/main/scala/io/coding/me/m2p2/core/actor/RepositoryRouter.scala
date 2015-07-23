package io.coding.me.m2p2.core.actor

import akka.actor.ActorSystem
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import scala.concurrent.duration._
import akka.pattern.ask


/**
 * Companion object grouping the repository messages that form the API.
 */
object RepositoryRouter {

  /**
   * Factory method for the actor system
   */
  def props(): Props = Props(new RepositoryRouter())

  
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
}

/**
 * Forms the route node of the P2 repository system.
 */
class RepositoryRouter extends Actor with ActorLogging {
  
  import RepositoryRouter._
  
  /**
   * 
   */
  var internalRepositories = Map.empty[RepositoryId, ActorRef]
  
  override def receive = {
    
    case ListRepositoriesRequest => 
      sender ! ListRepositoriesResponse(internalRepositories.keySet)
  }
}