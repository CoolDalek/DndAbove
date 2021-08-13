import akka.actor.ActorSystem
import com.softwaremill.macwire._
import config.ServerConfig
import controllers.PageController
import http.Controller
import logging.ScribeConfigurator
import monix.execution.Scheduler
import pureconfig.{ConfigObjectSource, ConfigSource}
import server.impl._
import services._
import services.impl._

import scala.util.Try

trait Module extends ScribeConfigurator {

  def bootstrap(): Try[Unit] = Try {
    scribe.info("Starting application.")

    implicit val sch: Scheduler = concurrent.global.GlobalScheduler

    implicit val classicSystem: ActorSystem = ActorSystem("Server")

    val configObject: ConfigObjectSource = ConfigSource.default

    implicit val serverConfig: ServerConfig = configObject.at("server").loadOrThrow[ServerConfig]

    val service: PageService = wire[PageServiceImpl]

    val pageController: Controller = wire[PageController]

    val controllers = List(pageController)

    wire[ServerImpl].start()
  }

}