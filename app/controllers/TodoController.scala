package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import lib.model.Todo

import model.ViewValueHome
import model.TodoModel
import model.TodoCategoryModel

import forms.TodoForm
import forms.TodoEditData

import presenters.TodoListPresenter
import presenters.TodoPresenter
import presenters.TodoFormPresenter

class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {
  def index() = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo一覧",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    for {
      todoList <- TodoModel.all
      categories <- TodoCategoryModel.all
    } yield {
      val presenter = new TodoListPresenter(todoList, categories)
      val flash = req.flash.get("success").getOrElse("")
      Ok(views.html.todo.Index(vv, presenter, flash))
    }
  }

  def add() = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    for {
      presenter <- getFormPresenter
    } yield {
      val form = TodoForm.add
      Ok(views.html.todo.Add(vv, form, presenter))
    }
  }

  def create() = Action.async { implicit req =>
    val form = TodoForm.add

    form.bindFromRequest.fold(
      formWithErrors => {
        val vv = ViewValueHome(
          title  = "Todo追加",
          cssSrc = Seq("reset.css", "main.css"),
          jsSrc  = Seq("main.js")
        )

        for {
          presenter <- getFormPresenter
        } yield {
          // binding failure, you retrieve the form containing errors:
          BadRequest(views.html.todo.Add(vv, formWithErrors, presenter))
        }
      },
      todoData => {
        for {
          id <- TodoModel.create(todoData.title, todoData.body, todoData.categoryId)
        } yield {
          /* binding success, you get the actual value. */
          Redirect(routes.TodoController.show(id))
        }
      }
    )
  }

  def edit(id: Long) = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo編集",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    for {
      todo      <- getTodo(id)
      presenter <- getFormPresenter
    } yield {
      val form = TodoForm.edit.fill(TodoEditData(todo.v.title, todo.v.body, todo.v.state.code, todo.v.categoryId))
      Ok(views.html.todo.Edit(vv, form, presenter, id))
    }
  }

  def update(id: Long) = Action.async { implicit req => 
    val form = TodoForm.edit

    form.bindFromRequest.fold(
      formWithErrors => {
        val vv = ViewValueHome(
          title  = "Todo編集",
          cssSrc = Seq("reset.css", "main.css"),
          jsSrc  = Seq("main.js")
        )

        for {
          presenter <- getFormPresenter
        } yield {
          // binding failure, you retrieve the form containing errors:
          BadRequest(views.html.todo.Edit(vv, formWithErrors, presenter, id))
        }
      },
      todoData => {
        for {
          todo <- getTodo(id)
          _    <- TodoModel.update(todo, todoData.title, todoData.body, todoData.state.toShort, todoData.categoryId)
        } yield {
          Redirect(routes.TodoController.show(id))
        }
      }
    )
  }

  def show(id: Long) = Action.async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo詳細",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    for {
      todo <- getTodo(id)
      category <- TodoCategoryModel.get(todo.v.categoryId)
    } yield {
      val presenter = new TodoPresenter(todo, category)
      Ok(views.html.todo.Show(vv, presenter))
    }
  }

  def remove(id: Long) = Action.async { implicit req =>
    for {
      todo <- TodoModel.remove(id)
    } yield {
      todo match {
        case None => Redirect(routes.TodoController.index)
        case _    => Redirect(routes.TodoController.index).flashing("success" -> "Todoを削除しました。")
      }
    }
  }

  private def getTodo(id: Long): Future[Todo.EmbeddedId] = {
    TodoModel.get(id).map(
      _ match {
        case Some(todo) => todo
        case _ => throw new Exception // TODO 404ページに遷移
      }
    )
  }

  private def getFormPresenter(): Future[TodoFormPresenter] = {
    for {
      todoCategories <- TodoCategoryModel.all
    } yield {
      new TodoFormPresenter(todoCategories)
    }
  }
}
