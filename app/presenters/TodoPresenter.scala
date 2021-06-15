package presenters

import lib.model.Todo
import lib.model.TodoCategory

import helpers.TodoHelper

class TodoPresenter(todo: Todo.EmbeddedId, category: Option[TodoCategory.EmbeddedId]) {
  case class TodoItem(id: Long, title: String, body: String, state: Todo.Status, categoryName: String)

  def item(): TodoItem = {
    val categoryName = category.map(_.v.name).getOrElse("")
    TodoItem(todo.id, todo.v.title, todo.v.body, todo.v.state, categoryName)
  }

  def stateStyle(): String = {
    TodoHelper.stateStyle(todo.v.state)
  }
}
