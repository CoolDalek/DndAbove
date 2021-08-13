package concurrent

import monix.execution.schedulers.SchedulerService

import scala.concurrent.duration._
import scala.sys.ShutdownHookThread

object SchedulerShutdown {

  def registerOnJVMShutdown(scheduler: SchedulerService, name: String): ShutdownHookThread =
    sys.addShutdownHook {
      scribe.debug(s"Shutting down scheduler $name.")
      scheduler.shutdown()
      scheduler.awaitTermination(1 minute span)
      scribe.debug(s"Scheduler $name terminated.")
    }

}