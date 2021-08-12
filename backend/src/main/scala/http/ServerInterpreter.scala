package http

import logging.ScribeServerLogging
import sttp.tapir.server.akkahttp.{AkkaHttpServerInterpreter, AkkaHttpServerOptions}

object ServerInterpreter {

  private val interpreter = AkkaHttpServerInterpreter(
    AkkaHttpServerOptions.customInterceptors(
      exceptionHandler = Some(HttpExceptionHandler),
      serverLog = Some(ScribeServerLogging),
    )
  )

  def apply(): AkkaHttpServerInterpreter = interpreter

}