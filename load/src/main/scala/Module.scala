import akka.actor.ActorSystem
import controllers.PageController
import server.impl.ServerImpl
import services.impl.PageServiceImpl

trait Module {

  def bootstrap(): Unit = {

    implicit val classicSystem: ActorSystem = ActorSystem("Server")

    val service = new PageServiceImpl
    val controller = new PageController(service)
    val server = new ServerImpl(controller)
    server.start()
  }

}