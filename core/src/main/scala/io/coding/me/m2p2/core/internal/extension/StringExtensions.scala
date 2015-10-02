package io.coding.me.m2p2.core.internal.extension

object StringExtensions {

  class StringExtension(val value: String) {

    def isNullOrEmpty(): Boolean = value == null || value.trim.isEmpty
    def isNotNullOrEmpty() = !isNullOrEmpty()
  }

  implicit def string2extension(value: String): StringExtension = new StringExtension(value)
}