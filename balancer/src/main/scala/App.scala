import scala.util.{Failure, Success}

object App extends Module {

  def main(args: Array[String]): Unit =
    bootstrap() match {
      case Failure(exc) =>
        scribe.error("Error during application startup.", exc)
      case Success(_) =>
        scribe.info("Application started.")
    }

}