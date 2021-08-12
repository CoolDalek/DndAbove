package logging

import akka.event.LoggingAdapter
import sttp.tapir.Endpoint
import sttp.tapir.model.ServerResponse
import sttp.tapir.server.interceptor.DecodeFailureContext
import sttp.tapir.server.interceptor.log.ServerLog

import scala.concurrent.Future

object ScribeServerLogging extends ServerLog[LoggingAdapter => Future[Unit]] {

  private def ignoreAdapter(log: => Unit): LoggingAdapter => Future[Unit] = _ => Future.successful(log)

  private def formattedDecodeFailure(ctx: DecodeFailureContext) =
    s"Decoding failure.\n${ctx.endpoint.showDetail}\nRequest: ${ctx.request}.\nInput: ${ctx.failingInput.show}.\nError: ${ctx.failure}."

  override def decodeFailureNotHandled(ctx: DecodeFailureContext): LoggingAdapter => Future[Unit] =
    ignoreAdapter {
      scribe.error(
        formattedDecodeFailure(ctx)
      )
    }

  override def decodeFailureHandled(ctx: DecodeFailureContext, response: ServerResponse[_]): LoggingAdapter => Future[Unit] =
    ignoreAdapter {
      scribe.warn(
        s"${formattedDecodeFailure(ctx)}\nRespond with: $response."
      )
    }

  override def requestHandled(e: Endpoint[_, _, _, _], statusCode: Int): LoggingAdapter => Future[Unit] =
    ignoreAdapter {
      scribe.debug(s"Handled request.\n${e.showDetail}\nStatus code: $statusCode.")
    }

  override def exception(e: Endpoint[_, _, _, _], ex: Throwable): LoggingAdapter => Future[Unit] =
    ignoreAdapter {
      scribe.error(s"Exception during request processing.\n${e.showDetail}", ex)
    }

}