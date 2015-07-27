package io.coding.me.m2p2.core.actor

import akka.actor.ActorSystem
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import scala.concurrent.duration._
import akka.pattern.ask
import kamon.Kamon

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
   * Stores all registered repositories
   */
  var internalRepositories = Map.empty[RepositoryId, ActorRef]

  override def preStart() = {
    
    // TODO: This is the most ugly place to start Kamon - anyway: Kamon requires it!
    Kamon.start()
  }
  
  override def postStop() = {

    // TODO: This is the most ugly place to stop Kamon - anyway: Kamon requires it!
    Kamon.shutdown()
  }
  
  override def receive = {

    case DeleteRepositoryRequest(id) =>
      if (!internalRepositories.contains(id)) {

        sender ! DeleteRepositoryResponse(id, false, Some("Repository does not exist"))

      } else {

        context.stop(internalRepositories(id))
        internalRepositories = internalRepositories - id
        sender ! DeleteRepositoryResponse(id, true, None)
      }

    case CreateRepositoryRequest(id) =>

      if (!internalRepositories.contains(id)) {

        internalRepositories = internalRepositories + (id -> context.actorOf(RepositoryReceptionist.props(id)))
        sender ! CreateRepositoryResponse(id, false)

      } else {

        sender ! CreateRepositoryResponse(id, true)
      }

    case ListRepositoriesRequest =>
      sender ! ListRepositoriesResponse(internalRepositories.keySet)
      
    case rr:RepositoryRequest => 

      // forward any repository related request to its corresponding repository
      internalRepositories.get(rr.id).foreach( _.forward(rr) )
  }
}