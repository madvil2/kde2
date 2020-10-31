package animals.schema

import animals.dto.ShelterDTO
import animals.errors.BadRequest
import org.squeryl.{KeyedEntity, Schema, Table}
import org.squeryl.PrimitiveTypeMode._
import cats.implicits._


case class Shelter(id: Long = 0,
                   address: String,
                   file_id: Option[Long],
                   organization_id: Long,
                   boss_id: Long) extends KeyedEntity[Long]
object ShelterSchema extends Schema {
  val shelter = table[Shelter]("shelter")

  val shelterWorkers = table[SourceToTarget]("account_shelter")
}


object Shelter {
  implicit class ToShelterDTO(shelter: Shelter) {
    import shelter._

    def toDTO = ShelterDTO(id, address, file_id, organization_id, boss_id)

  }
}