package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.util.Success
import scala.util.Failure
import scala.concurrent.duration.Duration

import lib.model.Todo

import model.ViewValueHome
import model.TodoModel
import model.TodoCategoryModel

import forms.TodoForm
import forms.TodoEditData

import helpers.TodoFormHelper

class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {
  def index() = Action { implicit req =>
    val todoListResult = Await.ready(TodoModel.all, Duration.Inf)
    val todoList = todoListResult.value.get match {
      case Success(list) => list
      case Failure(e) => throw e
    }

    val vv = ViewValueHome(
      title  = "Todo一覧",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.todo.Index(vv, todoList))
  }

  def add() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    val form = TodoForm.add
    val helper = Await.ready(getFormHelper, Duration.Inf).value.get match {
      case Success(helper) => helper
      case Failure(e) => throw e
    }

    Ok(views.html.todo.Add(vv, form, helper))
  }

  def create() = Action { implicit req =>
    val form = TodoForm.add

    form.bindFromRequest.fold(
      formWithErrors => {
        val vv = ViewValueHome(
          title  = "Todo追加",
          cssSrc = Seq("reset.css", "main.css"),
          jsSrc  = Seq("main.js")
        )

        val helper = Await.ready(getFormHelper, Duration.Inf).value.get match {
          case Success(helper) => helper
          case Failure(e) => throw e
        }

        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.todo.Add(vv, formWithErrors, helper))
      },
      todoData => {
        /* binding success, you get the actual value. */
        val createResult = Await.ready(TodoModel.create(todoData.title, todoData.body, todoData.categoryId), Duration.Inf)
        val id = createResult.value.get match {
          case Success(id) => id
          case Failure(e) => throw e
        }
      
        Redirect(routes.TodoController.show(id))
      }
    )
  }

  def edit(id: Long) = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo編集",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    val todo = Await.ready(getTodo(id), Duration.Inf).value.get match {
      case Success(t) => t
      case Failure(e) => throw e
    }

    val form = TodoForm.edit.fill(TodoEditData(todo.v.title, todo.v.body, todo.v.state.code, todo.v.categoryId))
    val helper = Await.ready(getFormHelper, Duration.Inf).value.get match {
      case Success(helper) => helper
      case Failure(e) => throw e
    }

    Ok(views.html.todo.Edit(vv, form, helper, id))
  }

  def update(id: Long) = Action { implicit req => 
    val form = TodoForm.edit

    form.bindFromRequest.fold(
      formWithErrors => {
        val vv = ViewValueHome(
          title  = "Todo追加",
          cssSrc = Seq("reset.css", "main.css"),
          jsSrc  = Seq("main.js")
        )

        val helper = Await.ready(getFormHelper, Duration.Inf).value.get match {
          case Success(helper) => helper
          case Failure(e) => throw e
        }

        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.todo.Edit(vv, formWithErrors, helper, id))
      },
      todoData => {
        Await.ready(
          for {
            todo <- getTodo(id)
            _    <- TodoModel.update(todo, todoData.title, todoData.body, todoData.state.toShort, todoData.categoryId)
          } yield {

          }
          ,Duration.Inf
        )
      
        Redirect(routes.TodoController.show(id))
      }
    )
  }

  def show(id: Long) = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo詳細",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    val todo = Await.ready(getTodo(id), Duration.Inf).value.get match {
      case Success(t) => t
      case Failure(e) => throw e
    }

    Ok(views.html.todo.Show(vv, todo))
  }

  private def getTodo(id: Long): Future[Todo.EmbeddedId] = {
    TodoModel.get(id).map(
      _ match {
        case Some(todo) => todo
        case _ => throw new Exception // TODO 404ページに遷移
      }
    )
  }

  private def getFormHelper(): Future[TodoFormHelper] = {
    for {
      todoCategories <- TodoCategoryModel.all
    } yield {
      new TodoFormHelper(todoCategories)
    }
  }
}
