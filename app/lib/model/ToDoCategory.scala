package lib.model

import ixias.model._
import java.time.LocalDateTime

import ToDoCategory._
case class ToDoCategory(
  id:        Option[Id],
  name:      String,
  slug:      String,
  color:     Short,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

object ToDoCategory {
  val  Id = the[Identity[Id]]
  type Id = Long @@ ToDoCategory
  type WithNoId = Entity.WithNoId [Id, ToDoCategory]
  type EmbeddedId = Entity.EmbeddedId[Id, ToDoCategory]

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(name: String, slug: String, color: Short): WithNoId = {
    new Entity.WithNoId(
      new ToDoCategory(
        id    = None,
        name  = name,
        slug  = slug,
        color = color
      )
    )
  }
}