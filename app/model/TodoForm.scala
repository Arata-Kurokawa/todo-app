package model

import ViewValueTodoList._

import lib.model.Todo
import lib.model.TodoCategory

case class ViewValueTodoAddForm(categories: Seq[TodoCategory.EmbeddedId], home: ViewValueHome) {
}

case class ViewValueTodoEditForm(id: Todo.Id, categories: Seq[TodoCategory.EmbeddedId], home: ViewValueHome) {
}
