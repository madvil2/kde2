package animals.dto

import animals.dao.{AccountDAO, FileDAO, OrganizationDAO}
import animals.errors.BadRequest
import animals.schema.{Account, Shelter}
import cats.implicits._

case class ShelterDTO(id: Option[Long],
                      address: Option[String],
                      fileId: Option[Long] = None,
                      file: Option[FileDTO] = None,
                      organizationId: Option[Long] = None,
                      organization: Option[OrganizationDTO] = None,
                      bossId: Option[Long] = None,
                      boss: Option[AccountDTO] = None
                     )
object ShelterDTO {
  def apply(id: Long,
            address: String,
            fileId: Option[Long],
            organizationId: Long,
            bossId: Long): ShelterDTO = {
    val org = OrganizationDAO.organizationById(organizationId)
    val boss = AccountDAO.accountById(bossId).some
    val file = fileId.map(FileDAO.fileById(_))
    new ShelterDTO(id = id.some, address = address.some, file = file, organization = org, boss = boss)

  }
  implicit class ToShelter(shelter: ShelterDTO) {

    def toShelter = Shelter(shelter.id.getOrElse(0),
      shelter.address.getOrElse(throw BadRequest("no address")),
      shelter.fileId,
      shelter.organizationId.getOrElse(throw BadRequest("no orgId")),
      shelter.bossId.getOrElse(throw BadRequest("no boss id")),
    )
  }
}
