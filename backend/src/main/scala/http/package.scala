import http.Controller.CanRegisterEndpoint
import http.Exceptions.NotImplemented
import monix.eval.Task
import monix.execution.Scheduler
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.{Tapir, Endpoint => TapirEndpoint}

import scala.concurrent.Future

package object http extends Tapir {

  type Endpoint[I, O] = ServerEndpoint[I, Nothing, O, Any, Future]

  type IncompleteEndpoint[I, O] = TapirEndpoint[I, Nothing, O, Any]

  val builder: IncompleteEndpoint[Unit, Unit] = infallibleEndpoint

  implicit class EndpointRegisterExtension[I, O](private val self: Endpoint[I, O]) extends AnyVal {

    def register()(implicit registrar: CanRegisterEndpoint): Endpoint[I, O] =
      EndpointExtensions.register(self)

  }

  implicit class ConformAndAttach[I, O](private val self: IncompleteEndpoint[I, O]) extends AnyVal {

    def conformAndAttach(logic: I => Task[O])
                        (implicit sch: Scheduler,
                         registrar: CanRegisterEndpoint): Endpoint[I, O] =
      EndpointExtensions.conformAndAttach(self, logic)

  }

  implicit class EndpointLogicExtension[I, O](private val self: IncompleteEndpoint[I, O])
    extends AnyVal {

    def notImplemented(implicit sch: Scheduler,
                       registrar: CanRegisterEndpoint): Endpoint[I, O] =
      self.conformAndAttach(_ => Task.raiseError(NotImplemented()))

    def lazyLogic(serverLogic: I => Task[O])
                 (implicit sch: Scheduler,
                  registrar: CanRegisterEndpoint): Endpoint[I, O] =
      self.conformAndAttach(serverLogic)

    def ignoreInput(serverLogic: => Task[O])
                   (implicit sch: Scheduler,
                    registrar: CanRegisterEndpoint): Endpoint[I, O] =
      self.conformAndAttach(_ => serverLogic)

  }

  implicit class EndpointUnitLogicExtension[I](private val self: IncompleteEndpoint[I, Unit])
    extends AnyVal {

    def emptyLogic(implicit sch: Scheduler,
                   registrar: CanRegisterEndpoint): Endpoint[I, Unit] =
      self.conformAndAttach(_ => Task.unit)

  }

}