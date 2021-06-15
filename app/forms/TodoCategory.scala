package forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

case class TodoCategoryData(name: String, slug: String, color: Int)

object TodoCategoryForm {
  def apply(): Form[TodoCategoryData] = {
    Form(
      mapping(
        "name"  -> nonEmptyText,
        "slug"  -> nonEmptyText,
        "color" -> number
      )(TodoCategoryData.apply)(TodoCategoryData.unapply)
    )
  }
}
