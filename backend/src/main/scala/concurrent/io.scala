package concurrent

import logging.ScribeExceptionReporter
import monix.execution.Scheduler
import monix.execution.schedulers.SchedulerService

object io {

  implicit val IOScheduler: SchedulerService = {
    val name = "io"
    val sch = Scheduler.io(
      name = name,
      reporter = new ScribeExceptionReporter(name),
    )
    SchedulerShutdown.registerOnJVMShutdown(sch, name)
    sch
  }

}