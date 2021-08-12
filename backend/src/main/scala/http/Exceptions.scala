package http

import http.Descriptions.ErrorDescription
import sttp.model.StatusCode

object Exceptions {

  implicit class ExceptionsOps(private val self: Throwable) extends AnyVal {

    def getReason: Option[Throwable] = Option(self.getCause)

  }

  abstract class AbstractHttpException(
                                        val statusCode: StatusCode,
                                        val errorMessage: ErrorDescription,
                                        private val reason: Option[Throwable] = None
                                      )
    extends RuntimeException(s"Code: ${statusCode.code}, message: ${errorMessage.asString}") {

    reason.foreach(initCause)

  }

  class InternalServerError(reason: Option[Throwable]) extends AbstractHttpException(
    statusCode = StatusCode.InternalServerError,
    errorMessage = Descriptions.InternalServerError,
    reason = reason
  )
  
  object InternalServerError {

    def apply(reason: Throwable): InternalServerError = new InternalServerError(Some(reason))

    def apply(): InternalServerError = new InternalServerError(None)
    
  }

  object NotImplemented extends AbstractHttpException(StatusCode.NotImplemented, Descriptions.NotImplemented)

}