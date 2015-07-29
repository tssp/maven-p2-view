package io.coding.me.m2p2.core.internal.metric

import kamon.metric.EntityRecorderFactory
import kamon.metric.instrument.InstrumentFactory
import kamon.metric.GenericEntityRecorder
import kamon.metric.instrument.Time
import kamon.Kamon

class FileAnalyzerMetrics(instrumentFactory: InstrumentFactory) extends GenericEntityRecorder(instrumentFactory) {

  val files = counter("file-counter")
  val errors = counter("file-counter")
  val processingTime = histogram("processing-time", Time.Microseconds)
  
}

object FileAnalyzerMetrics extends EntityRecorderFactory[FileAnalyzerMetrics] {

  def apply(name: String) = Kamon.metrics.entity(FileAnalyzerMetrics, name)

  def category: String = "file-analyzer"
  def createRecorder(instrumentFactory: InstrumentFactory): FileAnalyzerMetrics = new FileAnalyzerMetrics(instrumentFactory)
}
