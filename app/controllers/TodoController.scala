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

import model.ViewValueHome
import model.TodoModel
import model.TodoCategoryModel
import forms.TodoForm

class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {
  def index() = Action { implicit req =>
    val todoListResult = Await.ready(TodoModel.all, Duration.Inf)
    val todoList = todoListResult.value.get match {
      case Success(list) => list
      case Failure(e) => throw e
    }

    val vv = ViewValueHome(
      title  = "Todo一覧",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.todo.Index(vv, todoList))
  }

  def add() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val form = TodoForm()

    val todoCategoryOptionsResult = Await.ready(getTodoCategoryOptions, Duration.Inf)
    val todoCategoryOptions = (todoCategoryOptionsResult.value.get match {
      case Success(options) => options
      case Failure(e) => throw e
    })

    Ok(views.html.todo.Add(vv, form, todoCategoryOptions))
  }

  def create() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val form = TodoForm()

    val todoCategoryOptionsResult = Await.ready(getTodoCategoryOptions, Duration.Inf)
    val todoCategoryOptions = (todoCategoryOptionsResult.value.get match {
      case Success(options) => options
      case Failure(e) => throw e
    })

    form.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.todo.Add(vv, formWithErrors, todoCategoryOptions))
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

  def show(id: Long) = Action { implicit req =>
    val todoResult = Await.ready(TodoModel.get(id), Duration.Inf)
    val todo = todoResult.value.get match {
      case Success(todoOpt) => {
        todoOpt match {
          case Some(todo) => todo
          case _ => throw new Exception // TODO 404ページに遷移
        }
      }
      case Failure(e) => throw e
    }

    val vv = ViewValueHome(
      title  = "Todo詳細",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.todo.Show(vv, todo))
  }

  private def getTodoCategoryOptions(): Future[Seq[(String, String)]] = {
    // val todoCategoriesResult = Await.ready(TodoModel.all, Duration.Inf)
    TodoCategoryModel.all.map { list =>
      list.map(tc => (tc.id.toString, tc.v.name))
    }
  }
}
