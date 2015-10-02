package io.coding.me.m2p2.core.internal

import scala.concurrent.duration.Duration
import kamon.metric.instrument.Histogram

package object metric {

  import scala.concurrent.duration._

  class HistogramExtension(val histogram: Histogram) {

    def recordTime[T](f: => T): T = {

      val start = Duration(System.nanoTime(), NANOSECONDS)
      val ret = f
      val end = Duration(System.nanoTime(), NANOSECONDS)

      val delta = (end - start).toMicros

      histogram.record(delta)

      ret
    }
  }

  implicit def convert2extension(histogram: Histogram): HistogramExtension = new HistogramExtension(histogram)
}