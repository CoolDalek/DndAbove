package services

import monix.eval.Task


trait PageService {

  def healthCheck: Task[String]

}