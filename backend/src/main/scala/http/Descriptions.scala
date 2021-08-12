package http

import io.estatico.newtype.macros.newtype

object Descriptions {

  @newtype case class ErrorDescription(asString: String)

  val InternalServerError: ErrorDescription = ErrorDescription("Internal Server Error")

  val NotImplemented: ErrorDescription = ErrorDescription("Not Implemented")

}