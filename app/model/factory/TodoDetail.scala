package model.factory

import lib.model.Todo
import lib.model.TodoCategory

import model.ViewValueHome
import model.ViewValueTodoDetail

object ViewValueTodoDetailFactory {
  def create(todo: Todo.EmbeddedId, category: Option[TodoCategory.EmbeddedId]): ViewValueTodoDetail = {
    val home = ViewValueHome(
      title  = "Todo詳細",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    val categoryName = category.map(_.v.name).getOrElse("")
    val t = ViewValueTodoDetail.TodoItem(todo.id, todo.v.title, todo.v.body, todo.v.state, categoryName)
    ViewValueTodoDetail(t, home)
  }
}