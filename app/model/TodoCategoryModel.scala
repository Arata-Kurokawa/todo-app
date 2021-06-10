package model

import scala.concurrent.Future
import scala.concurrent.duration.Duration

import lib.persistence.onMySQL

import lib.model.TodoCategory
import lib.model.TodoCategory._

object TodoCategoryModel {
  def get(id: Long): Future[Option[EmbeddedId]] = {
    val repository = onMySQL.TodoCategoryRepository
    repository.get(Id(id))
  }

  def all(): Future[Seq[EmbeddedId]] = {
    val repository = onMySQL.TodoCategoryRepository
    repository.all()
  }
}
