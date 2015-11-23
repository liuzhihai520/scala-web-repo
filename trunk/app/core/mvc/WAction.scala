package core.mvc

import play.api.mvc._
import scala.concurrent.Future

object WAction extends ActionBuilder[ViewData] {
  def invokeBlock[A](request: Request[A], block: (ViewData[A]) => Future[Result]) = {
    val v = new ViewData(request).apply()
    block(v)
  }
}

object Action extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = block(request)
}