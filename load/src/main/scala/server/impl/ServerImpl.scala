package server.impl

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import config.ServerConfig
import controllers.PageController
import http.{Controller, Endpoint, Exceptions, HttpExceptionHandler, ServerInterpreter}
import logging.ScribeServerLogging
import server.Server
import sttp.tapir.server.akkahttp.{AkkaHttpServerInterpreter, AkkaHttpServerOptions}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.sys.ShutdownHookThread
import scala.util.{Failure, Success}

class ServerImpl(controllers: List[Controller])
                (implicit system: ActorSystem,
                 serverConfig: ServerConfig) extends Server {

  implicit val ec: ExecutionContextExecutor = system.dispatcher

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
          scribe.error(s"Exception during server starting, configuration: $serverConfig", exc)
        case Success(value) =>
          scribe.info(s"Server successfully started, configuration: $serverConfig")
          addStopHook(value)
      }
  }

  def addStopHook(binding: ServerBinding): ShutdownHookThread = {
    sys.addShutdownHook {
      for {
        _ <- binding.terminate(5 minutes span)
        _ <- stopSystem
      } yield ()
    }
  }

  def stopSystem(implicit ec: ExecutionContext): Future[Terminated] =
    for {
      _ <- system.terminate()
      terminated <- system.whenTerminated
    } yield terminated

}