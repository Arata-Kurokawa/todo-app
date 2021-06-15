package model

import lib.model.Todo

import ViewValueTodoDetail._

case class ViewValueTodoDetail(todo: TodoItem, home: ViewValueHome) {
}

object ViewValueTodoDetail {
  case class TodoItem(id: Todo.Id, title: String, body: String, state: Todo.Status, categoryName: String)
}
