package controllers

import services.PageService
import sttp.tapir._
import sttp.tapir.json.upickle._
import sttp.tapir.server.ServerEndpoint
import upickle.default._

import scala.concurrent.Future

class PageController(service: PageService) {

  def healthCheck: ServerEndpoint[Unit, Unit, String, Any, Future] =
    endpoint.get
      .in("health_check")
      .out(jsonBody[String])
      .serverLogic(_ => service.healthCheck)

}