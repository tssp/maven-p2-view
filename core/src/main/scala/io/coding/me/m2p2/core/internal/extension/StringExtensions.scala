package io.coding.me.m2p2.core.internal.extension

/**
 * @author tim@coding-me.com
 */
object StringExtensions {

  class StringExtension(val value: String) {

    def isNullOrEmpty(): Boolean = value == null || value.trim.isEmpty
    def isNotNullOrEmpty() = !isNullOrEmpty()
  }

  implicit def isNotNullOrEmpty(value: String): StringExtension = new StringExtension(value)
}