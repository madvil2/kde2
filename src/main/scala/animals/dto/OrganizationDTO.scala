package animals.dto

import animals.schema.Organization

case class OrganizationDTO(id: Option[Long],
                           name: String)

object OrganizationDTO {
  implicit class ToOrganization(org: OrganizationDTO){
    def toOrganization = Organization(org.id.getOrElse(0), org.name)
  }
}


