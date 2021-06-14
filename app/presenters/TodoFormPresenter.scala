package presenters

import lib.model.Todo
import lib.model.TodoCategory

class TodoFormPresenter(val categories: Seq[TodoCategory.EmbeddedId]) {
  def categoryOptions(): Seq[(String, String)] = {
    ("", "----------") +:categories.map(category => (category.id.toString, category.v.name))
  }

  def stateOptions(): Seq[(String, String)] = {
    Seq(
      (Todo.Status.WAITING.code.toString, Todo.Status.WAITING.name),
      (Todo.Status.IN_PROGRESS.code.toString, Todo.Status.IN_PROGRESS.name),
      (Todo.Status.COMPLETED.code.toString, Todo.Status.COMPLETED.name)
    )
  }
}
