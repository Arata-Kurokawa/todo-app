package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import model.ViewValueHome
import model.TodoCategoryModel

import presenters.TodoCategoryListPresenter

class TodoCategoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {
  def index() = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todoカテゴリ一覧",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    for {
      categories <- TodoCategoryModel.all
    } yield {
      val presenter = new TodoCategoryListPresenter(categories)
      Ok(views.html.todoCategory.Index(vv, presenter))
    }
  }

  def add() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todoカテゴリ作成",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    Ok(views.html.todoCategory.Add(vv))
  }

  def edit(id: Long) = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todoカテゴリ編集",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    Ok(views.html.todoCategory.Edit(vv))
  }

  def remove(id: Long) = Action { implicit req =>
    Redirect(routes.TodoCategoryController.index)
  }
}