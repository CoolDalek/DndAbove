package controllers

import http._
import monix.execution.Scheduler
import services.PageService
import sttp.tapir.json.upickle._

class PageController(service: PageService)
                    (implicit sch: Scheduler) extends Controller {

  val healthCheck: Endpoint[Unit, String] =
    builder.get
      .in("health_check")
      .out(jsonBody[String])
      .ignoreInput(service.healthCheck)

}