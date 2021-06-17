package model

import lib.model.Todo
import lib.model.TodoCategory

import ViewValueTodoList._

case class ViewValueTodoList(items: Seq[Item], home: ViewValueHome) {
}

object ViewValueTodoList {
  case class Item(id: Long, title: String, body: String, state: Todo.Status, categoryName: String)

  def apply(
    todoList: Seq[Todo.EmbeddedId],
    categories: Seq[TodoCategory.EmbeddedId]
  ): ViewValueTodoList = {
    val items = for {
      todo <- todoList
    } yield {
      val category = categories.find(_.id == todo.v.categoryId)
      ViewValueTodoList.Item(todo.id, todo.v.title, todo.v.body, todo.v.state, category.map(_.v.name).getOrElse(""))
    }

    val home = ViewValueHome(
      title  = "Todo一覧",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    ViewValueTodoList(items, home)
  }
}