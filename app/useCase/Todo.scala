
package useCase

import scala.concurrent.Future

import lib.persistence.onMySQL
import lib.model.Todo
import lib.model.Todo._
import lib.model.TodoCategory

object TodoUseCase {
  def get(id: Id): Future[Option[EmbeddedId]] = {
    val repository = onMySQL.TodoRepository
    repository.get(id)
  }

  def all(): Future[Seq[EmbeddedId]] = {
    val repository = onMySQL.TodoRepository
    repository.all()
  }

  def create(title: String, body: String, categoryId: Long): Future[Id] = {
    val repository = onMySQL.TodoRepository
    val entity = Todo(TodoCategory.Id(categoryId), title, body, Todo.Status.WAITING)
    repository.add(entity)
  }

  def update(todo: EmbeddedId, title: String, body: String, state: Short, categoryId: Long): Future[Option[EmbeddedId]] = {
    val repository = onMySQL.TodoRepository
    val newTodo = todo.v.copy(title = title, body = body, state = Todo.Status(state), categoryId = TodoCategory.Id(categoryId))
    repository.update(new Todo.EmbeddedId(newTodo))
  }

  def remove(id: Id): Future[Option[EmbeddedId]] = {
    val repository = onMySQL.TodoRepository
    repository.remove(id)
  }
}
