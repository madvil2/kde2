package animals.schema

import animals.dto.OrganizationDTO
import org.squeryl.{KeyedEntity, Schema, Table}
import org.squeryl.PrimitiveTypeMode._
import cats.implicits._

case class Organization(id: Long = 0,
                        name: String) extends KeyedEntity[Long] {
  def this() = this(0, "")
}

object Organization {
  implicit class ToOrganizationDTO(org: Organization){
    def toDTO = OrganizationDTO(org.id.some, org.name)
  }
}
object OrganizationSchema extends Schema{
  val organization = table[Organization]("organization")
}
