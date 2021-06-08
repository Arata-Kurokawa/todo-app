package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

import ToDo._
case class ToDo(
  id:         Option[Id],
  categoryId: Long,
  title:      String,
  body:       String,
  state:      Status,
  updatedAt:  LocalDateTime = NOW,
  createdAt:  LocalDateTime = NOW
) extends EntityModel[Id]

object ToDo {
  val  Id = the[Identity[Id]]
  type Id = Long @@ ToDo
  type WithNoId = Entity.WithNoId [Id, ToDo]
  type EmbeddedId = Entity.EmbeddedId[Id, ToDo]

  // ステータス定義
  //~~~~~~~~~~~~~~~~~
  sealed abstract class Status(val code: Short, val name: String) extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object WAITING     extends Status(code = 0,   name = "未着手")
    case object IN_PROGRESS extends Status(code = 100, name = "進行中")
    case object COMPLETED   extends Status(code = 200, name = "完了")
  }

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(categoryId: Long, title: String, body: String, state: Status): WithNoId = {
    new Entity.WithNoId(
      new ToDo(
        id         = None,
        categoryId = categoryId,
        title      = title,
        body       = body,
        state      = state
      )
    )
  }
}
