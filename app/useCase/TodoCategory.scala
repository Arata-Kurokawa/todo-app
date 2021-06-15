
package useCase

import scala.concurrent.Future
import scala.concurrent.duration.Duration

import lib.persistence.onMySQL

import lib.model.TodoCategory
import lib.model.TodoCategory._

object TodoCategoryUseCase {
  def get(id: Long): Future[Option[EmbeddedId]] = {
    val repository = onMySQL.TodoCategoryRepository
    repository.get(Id(id))
  }

  def all(): Future[Seq[EmbeddedId]] = {
    val repository = onMySQL.TodoCategoryRepository
    repository.all()
  }

  def create(name: String, slug: String): Future[Id] = {
    val repository = onMySQL.TodoCategoryRepository
    val entity = TodoCategory(name, slug, Color.RED)
    repository.add(entity)
  }

  def update(category: EmbeddedId, name: String, slug: String, color: Color): Future[Option[EmbeddedId]] = {
    val repository = onMySQL.TodoCategoryRepository
    val newCategory = category.v.copy(name = name, slug = slug, color = color)
    repository.update(new TodoCategory.EmbeddedId(newCategory))
  }
}
