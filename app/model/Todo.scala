package model

import scala.concurrent.Future
import scala.concurrent.duration.Duration

import lib.persistence.onMySQL
import lib.model.Todo._

object Todo {
  def get(id: Long): Future[Option[EmbeddedId]] = {
    val repository = onMySQL.TodoRepository
    onMySQL.TodoRepository.get(Id(id))
  }
}