package util

object Exceptions {

  implicit class ExceptionsOps(private val self: Throwable) extends AnyVal {

    def getReason: Option[Throwable] = Option(self.getCause)

  }

}