package io.coding.me.m2p2.api

import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration._

import com.typesafe.scalalogging.LazyLogging

import io.coding.me.m2p2.core.actor.RepositoryRouter
import io.coding.me.m2p2.core.actor.RepositoryRouter.RepositoryId

trait View {

  /**
   * Creates a new repository in the system. If the repository is known to the system an existing one will be returned.
   */
  def create(ident: String): Future[Option[Repository]]
}

object View {

  import akka.pattern._

  private class ViewImplementation(system: ActorSystem, owner: Boolean) extends View with LazyLogging {

    logger.info(s"Creating new M2P2 View (owner: ${owner})")

    implicit val context = system.dispatcher
    implicit val defaultTimeout = Timeout(10 seconds)
    implicit val router = system.actorOf(RepositoryRouter.props(), "m2p2-router")

    override def create(ident: String) = router.ask(RepositoryId(ident))
      .map { case r: Repository => Some(r) }
      .recover {
        case ex =>
          logger.warn(s"Could not create repository ${ident}", ex)
          None
      }
  }

  /**
   * Creates a new repository view.
   */
  def apply(): View = new ViewImplementation(ActorSystem(s"M2P2 - View"), true)

  /**
   * Creates a new repository view with a specific class loader, typically used in conjunction with container frameworks such as Plexus.
   */
  def apply(classLoader: ClassLoader): View = new ViewImplementation(ActorSystem(s"M2P2 - View", None, Some(classLoader)), true)

  def apply(system: ActorSystem): View = new ViewImplementation(system, false)
}