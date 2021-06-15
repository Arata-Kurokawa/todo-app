package model

import lib.model.Todo

import ViewValueTodoList._

case class ViewValueTodoList(items: Seq[Item], home: ViewValueHome) {
}

object ViewValueTodoList {
  case class Item(id: Long, title: String, body: String, state: Todo.Status, categoryName: String)
}
