package model

import scala.concurrent.Future
import scala.concurrent.duration.Duration

import lib.persistence.onMySQL

import lib.model.Todo
import lib.model.Todo._

object TodoModel {
  def get(id: Long): Future[Option[EmbeddedId]] = {
    val repository = onMySQL.TodoRepository
    repository.get(Id(id))
  }

  def all(): Future[Seq[EmbeddedId]] = {
    val repository = onMySQL.TodoRepository
    repository.all()
  }

  def create(title: String, body: String, categoryId: Int): Future[Id] = {
    val repository = onMySQL.TodoRepository
    val entity = Todo(categoryId, title, body, Todo.Status.WAITING)
    repository.add(entity)
  }
}
