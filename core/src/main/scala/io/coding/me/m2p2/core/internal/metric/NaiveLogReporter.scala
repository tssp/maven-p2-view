package io.coding.me.m2p2.core.internal.metric

import akka.actor.Actor
import akka.actor.ActorLogging
import kamon.metric.SubscriptionsDispatcher.TickMetricSnapshot
import akka.actor.ExtensionId
import akka.actor.ExtensionIdProvider
import akka.actor.Extension
import akka.actor.ExtendedActorSystem
import akka.actor.Props

// http://doc.akka.io/docs/akka/snapshot/scala/extending-akka.html
// https://github.com/kamon-io/Kamon/blob/master/kamon-log-reporter/src/main/scala/kamon/logreporter/LogReporter.scala

class NaiveLogReporterExtensionImpl(system: ExtendedActorSystem) extends Extension {
  
  val subscriber = system.actorOf(Props[NaiveLogReporter], "naive-log-reporter")
}

object NaiveLogReporterExtension extends ExtensionId[NaiveLogReporterExtensionImpl] with ExtensionIdProvider {
  
  //The lookup method is required by ExtensionIdProvider, so we return ourselves here, this allows us
  // to configure our extension to be loaded when the ActorSystem starts up
  override def lookup = NaiveLogReporterExtension
  
  //This method will be called by Akka to instantiate the Extension
  override def createExtension(system: ExtendedActorSystem) = new NaiveLogReporterExtensionImpl(system)
}

class NaiveLogReporter extends Actor with ActorLogging {
  
  log.info("Starting the Naive-Log-Reporter (Kamon) extension")
  
  def receive = {
    case tickSnapshot: TickMetricSnapshot =>
      
      val counters = tickSnapshot.metrics.filterKeys(_.category == "counter")
      val histograms = tickSnapshot.metrics.filterKeys(_.category == "histogram")

      println("#################################################")
      println("From: " + tickSnapshot.from)
      println("To: " + tickSnapshot.to)

      counters.foreach { case (e, s) =>
        val counterSnapshot = s.counter("counter").get
        println("Counter [%s] was incremented [%d] times.".format(e.name, counterSnapshot.count))
      }

      histograms.foreach { case (e, s) =>
        val histogramSnapshot = s.histogram("histogram").get
        println("Histogram [%s] has [%d] recordings.".format(e.name, histogramSnapshot.numberOfMeasurements))
      }
  }
}