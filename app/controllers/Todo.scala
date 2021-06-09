package controllers

import javax.inject._
import play.api.mvc._

import scala.concurrent.Await
import scala.util.Success
import scala.util.Failure

import model.ViewValueHome
import lib.persistence.onMySQL
import scala.concurrent.duration.Duration

import model.Todo

class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def index() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo Index",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.todo.Index(vv))
  }

  def show(id: Long) = Action { implicit req =>
    val todoResult = Await.ready(Todo.get(id), Duration.Inf)

    todoResult.value.get match {
      case Success(todo) => {
        todo match {
          case None => println("Not found 404")
          case _ => println("Some Todo")
        }
      }
      case Failure(e) => println(e.getMessage)
    }


    val vv = ViewValueHome(
      title  = "Todo Show",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.todo.Show(vv))
  }
}