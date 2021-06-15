package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import lib.model.TodoCategory

import forms.TodoCategoryForm
import forms.TodoCategoryData

import model.ViewValueHome
import useCase.TodoCategoryUseCase

import presenters.TodoCategoryListPresenter

class TodoCategoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {
  def index() = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todoカテゴリ一覧",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    for {
      categories <- TodoCategoryUseCase.all
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

    val form = TodoCategoryForm()

    Ok(views.html.todoCategory.Add(vv, form))
  }

  def create() = Action.async { implicit req =>
    val form = TodoCategoryForm()

    form.bindFromRequest.fold(
      formWithErrors => {
        val vv = ViewValueHome(
          title  = "Todo追加",
          cssSrc = Seq("reset.css", "main.css"),
          jsSrc  = Seq("main.js")
        )

        Future {
          BadRequest(views.html.todoCategory.Add(vv, formWithErrors))
        } 
      },
      todoCategoryData => {
        for {
          id <- TodoCategoryUseCase.create(todoCategoryData.name, todoCategoryData.slug)
        } yield {
          /* binding success, you get the actual value. */
          Redirect(routes.TodoCategoryController.index)
        }
      }
    )
  }

  def edit(id: Long) = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todoカテゴリ編集",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    for {
      category <- getTodoCategory(id)
    } yield {
      val form = TodoCategoryForm().fill(TodoCategoryData(category.v.name, category.v.slug))
      Ok(views.html.todoCategory.Edit(vv, form, id))
    }    
  }

  def update(id: Long) = Action.async { implicit req =>
    val form = TodoCategoryForm()

    form.bindFromRequest.fold(
      formWithErrors => {
        val vv = ViewValueHome(
          title  = "Todo編集",
          cssSrc = Seq("reset.css", "main.css"),
          jsSrc  = Seq("main.js")
        )

        Future {
          BadRequest(views.html.todoCategory.Edit(vv, formWithErrors, id))
        } 
      },
      todoCategoryData => {
        for {
          category <- getTodoCategory(id)
          id       <- TodoCategoryUseCase.update(category, todoCategoryData.name, todoCategoryData.slug)
        } yield {
          /* binding success, you get the actual value. */
          Redirect(routes.TodoCategoryController.index)
        }
      }
    )
  }

  def remove(id: Long) = Action { implicit req =>
    Redirect(routes.TodoCategoryController.index)
  }

  private def getTodoCategory(id: Long): Future[TodoCategory.EmbeddedId] = {
    TodoCategoryUseCase.get(id).map(
      _ match {
        case Some(todoCategory) => todoCategory
        case _ => throw new Exception // TODO 404ページに遷移
      }
    )
  }
}