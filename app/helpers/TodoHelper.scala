package helpers

import lib.model.Todo

object TodoHelper {
  def stateStyle(state: Todo.Status): String = {
    state match {
      case Todo.Status.WAITING => "todo_state__wating"
      case Todo.Status.IN_PROGRESS => "todo_state__inprogress"
      case Todo.Status.COMPLETED => "todo_state__completed"
    }
  }
}