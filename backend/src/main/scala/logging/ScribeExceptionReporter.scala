package logging

import monix.execution.UncaughtExceptionReporter

class ScribeExceptionReporter(poolName: String) extends UncaughtExceptionReporter {

  override def reportFailure(ex: Throwable): Unit =
    scribe.error(s"Uncaught exception in $poolName.", ex)

}