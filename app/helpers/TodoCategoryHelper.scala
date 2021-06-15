package helpers

import lib.model.TodoCategory

object TodoCategoryHelper {
  def colorOptions(): Seq[(String, String)] = {
    Seq(
      (TodoCategory.Color.RED.code.toString,   TodoCategory.Color.RED.name),
      (TodoCategory.Color.GREEN.code.toString, TodoCategory.Color.GREEN.name),
      (TodoCategory.Color.BLUE.code.toString,  TodoCategory.Color.BLUE.name)
    )
  }
}