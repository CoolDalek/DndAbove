package concurrent

import monix.execution.schedulers.SchedulerService

import scala.concurrent.duration._
import scala.sys.ShutdownHookThread

object SchedulerShutdown {

  def registerOnJVMShutdown(scheduler: SchedulerService): ShutdownHookThread =
    sys.addShutdownHook {
      scheduler.shutdown()
      scheduler.awaitTermination(1 minute span)
    }

}