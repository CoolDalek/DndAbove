package http

import http.Exceptions.{AbstractHttpException, InternalServerError}
import sttp.model.StatusCode
import sttp.tapir.server.interceptor.ValuedEndpointOutput
import sttp.tapir.server.interceptor.exception.{ExceptionContext, ExceptionHandler}

import scala.util.control.NonFatal

object HttpExceptionHandler extends ExceptionHandler {

  val InternalErrorResponse: (StatusCode, String) =
    (
      StatusCode.InternalServerError,
      Descriptions.InternalServerError.asString,
    )

  override def apply(ctx: ExceptionContext): Option[ValuedEndpointOutput[_]] = {
    val response = ctx.e match {

      case _: InternalServerError =>
        InternalErrorResponse

      case http: AbstractHttpException =>
        http.statusCode -> http.errorMessage.asString

      case NonFatal(_) =>
        InternalErrorResponse

    }

    Some(ValuedEndpointOutput(statusCode.and(stringBody), response))
  }

}