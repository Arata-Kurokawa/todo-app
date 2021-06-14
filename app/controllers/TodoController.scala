package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport
import play.api.libs.json._

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

import presenters.TodoFormPresenter

class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {
  def index() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo一覧",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    val f = for {
      todoList <- TodoModel.all
    } yield {
      Ok(views.html.todo.Index(vv, todoList))
    }

    Await.ready(f, Duration.Inf).value.get match {
      case Success(ok) => ok
      case Failure(e) => throw e
    }
  }

  def add() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    val f = for {
      presenter <- getFormPresenter
    } yield {
      val form = TodoForm.add
      Ok(views.html.todo.Add(vv, form, presenter))
    }

    Await.ready(f, Duration.Inf).value.get match {
      case Success(ok) => ok
      case Failure(e) => throw e
    }
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

        val f = for {
          presenter <- getFormPresenter
        } yield {
          // binding failure, you retrieve the form containing errors:
          BadRequest(views.html.todo.Add(vv, formWithErrors, presenter))
        }

        Await.ready(f, Duration.Inf).value.get match {
          case Success(badReq) => badReq
          case Failure(e) => throw e
        }
      },
      todoData => {
        var f = for {
          id <- TodoModel.create(todoData.title, todoData.body, todoData.categoryId)
        } yield {
          /* binding success, you get the actual value. */
          Redirect(routes.TodoController.show(id))
        }

        Await.ready(f, Duration.Inf).value.get match {
          case Success(redirect) => redirect
          case Failure(e) => throw e
        }
      }
    )
  }

  def edit(id: Long) = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo編集",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    val f = for {
      todo      <- getTodo(id)
      presenter <- getFormPresenter
    } yield {
      val form = TodoForm.edit.fill(TodoEditData(todo.v.title, todo.v.body, todo.v.state.code, todo.v.categoryId))
      Ok(views.html.todo.Edit(vv, form, presenter, id))
    }

    Await.ready(f, Duration.Inf).value.get match {
      case Success(ok) => ok
      case Failure(e) => throw e
    }
  }

  def update(id: Long) = Action { implicit req => 
    val form = TodoForm.edit

    form.bindFromRequest.fold(
      formWithErrors => {
        val vv = ViewValueHome(
          title  = "Todo編集",
          cssSrc = Seq("reset.css", "main.css"),
          jsSrc  = Seq("main.js")
        )

        val f = for {
          presenter <- getFormPresenter
        } yield {
          // binding failure, you retrieve the form containing errors:
          BadRequest(views.html.todo.Edit(vv, formWithErrors, presenter, id))
        }

        Await.ready(f, Duration.Inf).value.get match {
          case Success(badReq) => badReq
          case Failure(e) => throw e
        }
      },
      todoData => {
        val f = for {
          todo <- getTodo(id)
          _    <- TodoModel.update(todo, todoData.title, todoData.body, todoData.state.toShort, todoData.categoryId)
        } yield {
          Redirect(routes.TodoController.show(id))
        }

        Await.ready(f, Duration.Inf).value.get match {
          case Success(redirect) => redirect
          case Failure(e) => throw e
        }
      }
    )
  }

  def show(id: Long) = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo詳細",
      cssSrc = Seq("reset.css", "main.css"),
      jsSrc  = Seq("main.js")
    )

    val f = for {
      todo <- getTodo(id)
    } yield {
      Ok(views.html.todo.Show(vv, todo))
    }

    Await.ready(f, Duration.Inf).value.get match {
      case Success(ok) => ok
      case Failure(e) => throw e
    }
  }

  def remove(id: Long) = Action { implicit req =>
    val f = for {
      _ <- TodoModel.remove(id)
    } yield {
      Redirect(routes.TodoController.index)
    }

    Await.ready(f, Duration.Inf).value.get match {
      case Success(redirect) => redirect
      case Failure(e) => throw e
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
