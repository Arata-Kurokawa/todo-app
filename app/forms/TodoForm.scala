package forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

case class TodoData(title: String, body: String, categoryId: Int)

object TodoForm {
  def apply(): Form[TodoData] = {
    Form(
      mapping(
        "title"      -> nonEmptyText,
        "body"       -> nonEmptyText,
        "categoryId" -> number(min = 1)
      )(TodoData.apply)(TodoData.unapply)
    )
  }
}
