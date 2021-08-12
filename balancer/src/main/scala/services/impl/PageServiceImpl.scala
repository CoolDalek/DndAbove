package services.impl

import monix.eval.Task
import services.PageService

class PageServiceImpl extends PageService {

  override def healthCheck: Task[String] =
    Task.now("Hello World!")

}