package io.coding.me.m2p2

import com.typesafe.scalalogging.LazyLogging
import java.util.UUID
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import scala.concurrent.Future

class RepositorySystem(actorSystem: ActorSystem) extends LazyLogging {

  logger.info("Initializing repository system for Maven-P2-View")
  
  def create(id: String): Future[Option[RepositoryView]] = ???
  def delete(id: String): Future[Unit] = ???

}

object RepositorySystem extends LazyLogging {

  @volatile var internalSystem: Option[RepositorySystem] = None

  def apply(actorSystem: ActorSystem): RepositorySystem = this.synchronized {

    internalSystem match {

      case Some(system) => system
      case _ =>

        internalSystem = Some(new RepositorySystem(actorSystem))
        internalSystem.get
    }
  }

  def apply(name: Option[String] = None, classLoader: Option[ClassLoader] = None): RepositorySystem = this.synchronized {

    internalSystem match {

      case Some(system) => system
      case _ =>

        logger.info(s"Creating actor system for Maven-P2-View");

        val id = name.getOrElse(s"actor-system-m2p2-${UUID.randomUUID()}")
        val config = classLoader.map(ConfigFactory.defaultReference).getOrElse(ConfigFactory.defaultReference());

        val actorSystem = ActorSystem(id, config, classLoader.getOrElse(getClass.getClassLoader));

        internalSystem = Some(new RepositorySystem(actorSystem))
        internalSystem.get
    }
  }
}