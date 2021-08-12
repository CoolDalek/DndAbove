package logging

import akka.event.LoggingAdapter
import sttp.tapir.Endpoint
import sttp.tapir.model.ServerResponse
import sttp.tapir.server.interceptor.DecodeFailureContext
import sttp.tapir.server.interceptor.log.ServerLog

import scala.concurrent.Future

object ScribeServerLogging extends ServerLog[LoggingAdapter => Future[Unit]] {

  private def ignoreAdapter(log: => Unit): LoggingAdapter => Future[Unit] = _ => Future.successful(log)

  override def decodeFailureNotHandled(ctx: DecodeFailureContext): LoggingAdapter => Future[Unit] =
    ignoreAdapter {
      scribe.error(
        s"Decoding failure:\nendpoint: ${ctx.endpoint};\nrequest: ${ctx.request};\ninput: ${ctx.failingInput};\nerror: ${ctx.failure}."
      )
    }

  override def decodeFailureHandled(ctx: DecodeFailureContext, response: ServerResponse[_]): LoggingAdapter => Future[Unit] =
    ignoreAdapter {
      scribe.warn(
        s"Decoding failure:\nendpoint: ${ctx.endpoint};\nrequest: ${ctx.request};\ninput: ${ctx.failingInput};\nerror: ${ctx.failure}.\nRespondWith: $response"
      )
    }

  override def requestHandled(e: Endpoint[_, _, _, _], statusCode: Int): LoggingAdapter => Future[Unit] =
    ignoreAdapter {
      scribe.debug(s"Handled request, endpoint: $e, status code: $statusCode")
    }

  override def exception(e: Endpoint[_, _, _, _], ex: Throwable): LoggingAdapter => Future[Unit] =
    ignoreAdapter {
      scribe.error(s"Exception during request processing, endpoint: $e", ex)
    }

}