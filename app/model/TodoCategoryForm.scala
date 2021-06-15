package model

import lib.model.TodoCategory

case class ViewValueTodoCategoryForm(id: Option[TodoCategory.Id], home: ViewValueHome) {
  def isNew(): Boolean = {
    id.map(_ => false).getOrElse(true)
  }
}

object ViewValueTodoCategoryFormFactory {
  def createAdd(): ViewValueTodoCategoryForm = {
    val home = ViewValueHome(
      title  = "Todoカテゴリ作成",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    ViewValueTodoCategoryForm(None, home)
  }

  def createEdit(id: TodoCategory.Id): ViewValueTodoCategoryForm = {
    val home = ViewValueHome(
      title  = "Todoカテゴリ編集",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    ViewValueTodoCategoryForm(Option(id), home)
  }
}