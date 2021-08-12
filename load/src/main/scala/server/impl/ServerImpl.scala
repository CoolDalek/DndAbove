package server.impl

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import controllers.PageController
import server.Server
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.sys.ShutdownHookThread
import scala.util.{Failure, Success}

class ServerImpl(pageController: PageController)
                (implicit system: ActorSystem) extends Server {

  implicit val ec: ExecutionContextExecutor = system.dispatcher

  override def start(): Unit = {
    val route = AkkaHttpServerInterpreter().toRoute(List(pageController.healthCheck))
    Http()
      .newServerAt("127.0.0.1", 8000)
      .bind(route)
      .onComplete {
        case Failure(exception) =>
          exception.printStackTrace()
        case Success(value) =>
          addStopHook(value)
      }
  }

  def addStopHook(binding: ServerBinding): ShutdownHookThread = {
    sys.addShutdownHook {
      binding
        .terminate(5 minutes)
        .flatMap(_ => stopSystem)
    }
  }

  def stopSystem(implicit ec: ExecutionContext): Future[Terminated] = {
    import system._
    terminate().flatMap(_ => whenTerminated)
  }

}