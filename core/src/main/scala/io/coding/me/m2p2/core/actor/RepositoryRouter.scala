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

}

/**
 * Forms the route node of the P2 repository system.
 */
class RepositoryRouter extends Actor with ActorLogging {

  /**
   *
   */
  var internalRepositories = Map.empty[RepositoryId, ActorRef]

  override def receive = {

    case CreateRepositoryRequest(id) =>

      if (!internalRepositories.contains(id)) {
        
        internalRepositories= internalRepositories + (id -> null) // TODO: 
        sender ! CreateRepositoryResponse(id, false)
      
      } else {
        
        sender ! CreateRepositoryResponse(id, true)
      }

    case ListRepositoriesRequest =>
      sender ! ListRepositoriesResponse(internalRepositories.keySet)
  }
}