package http

import http.Controller.CanRegisterEndpoint

import scala.collection.mutable

trait Controller {

  private val buffer = mutable.Set.empty[Endpoint[_, _]]

  protected implicit val CanRegisterEndpoint: CanRegisterEndpoint = new CanRegisterEndpoint {

    override def register(endpoint: Endpoint[_, _]): Unit = buffer += endpoint

  }

  def endpoints: List[Endpoint[_, _]] = buffer.to(List)

}
object Controller {

  sealed trait CanRegisterEndpoint {

    def register(endpoint: Endpoint[_, _]): Unit

  }

}