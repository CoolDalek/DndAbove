package services.impl

import services.PageService

import scala.concurrent.Future

class PageServiceImpl extends PageService {

  override def healthCheck: Future[Either[Unit, String]] =
    Future.successful(Right("Hello!"))

}