package services

import cats.effect.syntax.all._
import cats.effect.kernel._

import scala.concurrent.Future

trait PageService {

  def healthCheck: Future[Either[Unit, String]]

}