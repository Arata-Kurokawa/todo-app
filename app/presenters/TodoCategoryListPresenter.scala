package presenters

import lib.model.TodoCategory

class TodoCategoryListPresenter(categories: Seq[TodoCategory.EmbeddedId]) {
  case class TodoCategoryItem(id: Long, name: String, slug: String)

  def list(): Seq[TodoCategoryItem] = {
    for {
      category <- categories
    } yield {
      TodoCategoryItem(category.id, category.v.name, category.v.slug)
    }
  }
}