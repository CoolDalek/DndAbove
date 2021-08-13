package server.impl

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import config.ServerConfig
import http.{Controller, ServerInterpreter}
import monix.execution.Scheduler
import server.Server

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.sys.ShutdownHookThread
import scala.util.{Failure, Success}

class ServerImpl(controllers: List[Controller])
                (implicit system: ActorSystem,
                 serverConfig: ServerConfig,
                 sch: Scheduler) extends Server {

  override def start(): Unit = {
    val route = ServerInterpreter().toRoute(
      controllers.flatMap(_.endpoints)
    )

    Http().newServerAt(
      serverConfig.host,
      serverConfig.port,
    ).bind(route)
      .onComplete {
        case Failure(exc) =>
          scribe.error(s"Exception during server starting, configuration: $serverConfig.", exc)
          stopSystem
        case Success(value) =>
          scribe.info(s"Server successfully started, configuration: $serverConfig.")
          addStopHook(value)
      }
  }

  def addStopHook(binding: ServerBinding): ShutdownHookThread =
    sys.addShutdownHook {
      for {
        _ <- Future.successful(scribe.debug(s"Shutting down server."))
        _ <- binding.terminate(5 minutes span)
        _ <- stopSystem
      } yield scribe.debug("Server terminated.")
    }

  def stopSystem(implicit ec: ExecutionContext): Future[Unit] =
    for {
      _ <- Future.successful(scribe.debug(s"Shutting down actor system ${system.name}."))
      _ <- system.terminate()
      _ <- system.whenTerminated
    } yield scribe.debug(s"Actor system ${system.name} terminated.")

}