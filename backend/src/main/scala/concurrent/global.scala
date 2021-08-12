package concurrent

import logging.ScribeExceptionReporter
import monix.execution.Scheduler
import monix.execution.schedulers.SchedulerService

object global {

  implicit val GlobalScheduler: SchedulerService = {
    val name = "Global"
    Scheduler.forkJoin(
      parallelism = sys.runtime.availableProcessors(),
      maxThreads = sys.runtime.availableProcessors() * 2,
      name = name,
      reporter = new ScribeExceptionReporter(name),
    )
  }

  SchedulerShutdown.registerOnJVMShutdown(GlobalScheduler)

}