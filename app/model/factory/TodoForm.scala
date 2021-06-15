package model.factory

import lib.model.Todo
import lib.model.TodoCategory

import model.ViewValueHome
import model.ViewValueTodoAddForm
import model.ViewValueTodoEditForm

object ViewValueTodoFormFactory {
  def createAdd(categories: Seq[TodoCategory.EmbeddedId]): ViewValueTodoAddForm = {
    val home = ViewValueHome(
      title  = "Todo作成",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    ViewValueTodoAddForm(categories, home)
  }

  def createEdit(id: Todo.Id, categories: Seq[TodoCategory.EmbeddedId]): ViewValueTodoEditForm = {
    val home = ViewValueHome(
      title  = "Todo編集",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    ViewValueTodoEditForm(id, categories, home)
  }
}