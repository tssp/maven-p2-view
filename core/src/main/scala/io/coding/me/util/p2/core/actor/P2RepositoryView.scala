package io.coding.me.util.p2.core.actor

import akka.actor.ActorSystem
import akka.actor.ActorRef

/**
 *
 * @author tim@coding-me.com
 */
object P2RepositoryView {

  /**
   * Reference to the singleton actor system
   */
  var internalSystem: ActorSystem = null

  /**
   * Creates a P2 repository view without any environment references.
   */
  def apply(): ActorRef = {

    require(internalSystem == null)

    apply(ActorSystem("P2-Repository-View-System"))
  }

  /**
   * Creates a P2 repository view with a given actor system
   */
  def apply(system: ActorSystem): ActorRef = {

    require(internalSystem == null)

    internalSystem = system
    internalSystem.actorOf(P2RepositoryRouter.props(), "p2-repository-router")
  }

  /**
   * Creates a P2 repository view with a specific class loader, typically used in conjunction with container frameworks such as Plexus.
   */
  def apply(loader: ClassLoader): ActorRef = {

    require(internalSystem == null)

    internalSystem = ActorSystem("P2-Repository-View-System", None, Some(loader))
    internalSystem.actorOf(P2RepositoryRouter.props(), "p2-repository-router")
  }
}