package io.coding.me.m2p2.core.internal.metric

import com.codahale.metrics.MetricRegistry

object Registry {
  
  private lazy val metrics = new MetricRegistry()
    
  def apply() = metrics
}