import http.Controller.CanRegisterEndpoint
import http.Exceptions.NotImplemented
import monix.eval.Task
import monix.execution.{CancelableFuture, Scheduler}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.{Tapir, Endpoint => TapirEndpoint}

import scala.concurrent.Future

package object http extends Tapir {

  type Endpoint[I, O] = ServerEndpoint[I, Nothing, O, Any, Future]

  type IncompleteEndpoint[I, O] = TapirEndpoint[I, Nothing, O, Any]

  val builder: IncompleteEndpoint[Unit, Unit] = infallibleEndpoint

  protected sealed trait ConformAndAttach extends Any {

    protected def conformAndAttach[I, O](logic: I => Task[O],
                                       self: IncompleteEndpoint[I, O])(implicit sch: Scheduler): Endpoint[I, O] = {

      def conformed(input: I): CancelableFuture[Either[Nothing, O]] =
        logic(input).map { output =>
          Right[Nothing, O](output)
        }.runToFuture

      self.serverLogic(conformed)
    }

  }

  implicit class EndpointLogicExtension[I, O](private val self: IncompleteEndpoint[I, O])
    extends AnyVal with ConformAndAttach {

    private def conformAndAttach(logic: I => Task[O])(implicit sch: Scheduler): Endpoint[I, O] =
      conformAndAttach(logic, self)

    def notImplemented(implicit sch: Scheduler): Endpoint[I, O] =
      conformAndAttach(_ => Task.raiseError(NotImplemented))

    def lazyLogic(serverLogic: I => Task[O])(implicit sch: Scheduler): Endpoint[I, O] =
      conformAndAttach(serverLogic)

    def ignoreInput(serverLogic: => Task[O])(implicit sch: Scheduler): Endpoint[I, O] =
      conformAndAttach(_ => serverLogic)

  }

  implicit class EndpointUnitLogicExtension[I](private val self: IncompleteEndpoint[I, Unit])
    extends AnyVal with ConformAndAttach {

    private def conformAndAttach(logic: I => Task[Unit])(implicit sch: Scheduler): Endpoint[I, Unit] =
      conformAndAttach(logic, self)

    def emptyLogic(implicit sch: Scheduler): Endpoint[I, Unit] =
      conformAndAttach(_ => Task.unit)

  }

  implicit class EndpointBuilderExtension[I, O](private val self: Endpoint[I, O]) extends AnyVal {

    def build()(implicit registrar: CanRegisterEndpoint): Endpoint[I, O] = {
      registrar.register(self)
      self
    }

  }

}