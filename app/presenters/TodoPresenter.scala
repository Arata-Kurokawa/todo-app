package presenters

import lib.model.Todo
import lib.model.TodoCategory

class TodoPresenter(todo: Todo.EmbeddedId) {
  def stateStyle(): String = {
    todo.v.state match {
      case Todo.Status.WAITING => "todo-card_state__wating"
      case Todo.Status.IN_PROGRESS => "todo-card_state__inprogress"
      case Todo.Status.COMPLETED => "todo-card_state__completed"
    }
  }
}
