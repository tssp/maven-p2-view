package io.coding.me.m2p2.core.internal.metric

import kamon.metric.EntityRecorderFactory
import kamon.metric.instrument.InstrumentFactory
import kamon.metric.GenericEntityRecorder
import kamon.metric.instrument.Time
import kamon.Kamon

class ArtifactAnalyzerMetrics(instrumentFactory: InstrumentFactory) extends GenericEntityRecorder(instrumentFactory) {

  val files = counter("artifact-counter")
  val errors = counter("error-counter")
  val processingTime = histogram("processing-time", Time.Microseconds)
  
}

object ArtifactAnalyzerMetrics extends EntityRecorderFactory[ArtifactAnalyzerMetrics] {

  def apply(name: String) = Kamon.metrics.entity(ArtifactAnalyzerMetrics, name)

  def category: String = "artifact-analyzer"
  def createRecorder(instrumentFactory: InstrumentFactory): ArtifactAnalyzerMetrics = new ArtifactAnalyzerMetrics(instrumentFactory)
}
