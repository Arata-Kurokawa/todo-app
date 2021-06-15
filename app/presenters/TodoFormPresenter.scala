package presenters

import lib.model.Todo
import lib.model.TodoCategory

import helpers.TodoHelper

class TodoFormPresenter(val categories: Seq[TodoCategory.EmbeddedId]) {
}
