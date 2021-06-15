package presenters

import lib.model.Todo
import lib.model.TodoCategory

import helpers.TodoHelper

class TodoListPresenter(val todoList: Seq[Todo.EmbeddedId], val categories: Seq[TodoCategory.EmbeddedId]) {
  case class TodoItem(id: Long, title: String, body: String, state: Todo.Status, categoryName: String) {
    def stateStyle(): String = {
      TodoHelper.stateStyle(state)
    }
  }

  def list(): Seq[TodoItem] = {
    for {
      todo <- todoList
    } yield {
      val category = categories.find(_.id == todo.v.categoryId)
      TodoItem(todo.id, todo.v.title, todo.v.body, todo.v.state, category.map(_.v.name).getOrElse(""))
    }
  }
}
