package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport

import model.ViewValueHome
import model.TodoCategoryModel

class TodoCategoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {
  def index() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todoカテゴリ一覧",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    Ok(views.html.todoCategory.Index(vv))
  }

  def add() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todoカテゴリ作成",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    Ok(views.html.todoCategory.Add(vv))
  }
}