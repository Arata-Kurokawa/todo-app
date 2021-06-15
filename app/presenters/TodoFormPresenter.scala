package presenters

import lib.model.Todo
import lib.model.TodoCategory

import helpers.TodoHelper

class TodoFormPresenter(val categories: Seq[TodoCategory.EmbeddedId]) {
  def categoryOptions(): Seq[(String, String)] = {
    TodoHelper.categoryOptions(categories)
  }

  def stateOptions(): Seq[(String, String)] = {
    TodoHelper.stateOptions()
  }
}
