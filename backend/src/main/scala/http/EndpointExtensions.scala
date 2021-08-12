package http

import http.Controller.CanRegisterEndpoint
import monix.eval.Task
import monix.execution.{CancelableFuture, Scheduler}

object EndpointExtensions {

  def register[I, O](self: Endpoint[I, O])(implicit registrar: CanRegisterEndpoint): Endpoint[I, O] = {
    registrar.register(self)
    self
  }

  def conformAndAttach[I, O](self: IncompleteEndpoint[I, O],
                             logic: I => Task[O])
                            (implicit sch: Scheduler,
                             registrar: CanRegisterEndpoint): Endpoint[I, O] = {

    def conformed(input: I): CancelableFuture[Either[Nothing, O]] =
      logic(input).map { output =>
        Right[Nothing, O](output)
      }.runToFuture

    val attached: Endpoint[I, O] = self.serverLogic(conformed)
    register(attached)
  }

}