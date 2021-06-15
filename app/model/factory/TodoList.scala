package model.factory

import lib.model.Todo
import lib.model.TodoCategory

import model.ViewValueHome
import model.ViewValueTodoList

object ViewValueTodoListFactory {
  def create(
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