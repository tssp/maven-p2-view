package io.coding.me.util.p2.core.internal.resource

import java.io.Closeable
import scala.util.{ Try, Success, Failure }

/**
 * @author tim@coding-me.com
 */
object TryWithResource {

  class TryWithResourceImpl[A <: Closeable](resource: => A) extends TryWithResource[A] {

    override def flatMap[B](f: A => Try[B]): Try[B] = {

      var r: Closeable = null

      try {

        r = resource

        f(r.asInstanceOf[A])

      } catch {

        case t: Throwable => Failure(t)

      } finally {

        if (r != null) {

          Try(r.close())
        }
      }
    }

    override def map[B](f: A => B): Try[B] = {

      var r: Closeable = null

      try {

        r = resource

        Try(f(r.asInstanceOf[A]))

      } catch {

        case t: Throwable => Failure(t)

      } finally {

        if (r != null) {

          Try(r.close())
        }
      }
    }
  }

  def apply[A <: Closeable](resource: => A): TryWithResource[A] = new TryWithResourceImpl[A](resource)
}

trait TryWithResource[A <: Closeable] {

  def map[B](f: A => B): Try[B]

  def flatMap[B](f: A => Try[B]): Try[B]
}

