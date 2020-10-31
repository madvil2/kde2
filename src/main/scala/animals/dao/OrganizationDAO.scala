package animals.dao

import animals.dto.{OrganizationDTO, ShelterDTO}
import animals.errors.NotFound
import animals.schema.{Organization, ShelterSchema}
import animals.schema.OrganizationSchema._
import org.squeryl.PrimitiveTypeMode
import xitrum.scope.request.Params

object OrganizationDAO extends PrimitiveTypeMode {

  def organizationsList(offset: Int, limit: Int, params: Params) = inTransaction({
    from(organization)(org =>
      where(org.id isNotNull)
        select (org)
    )
      .page(offset, limit)
      .toList
      .map(_.toDTO)
  })

  def organizationById(id: Long): Option[OrganizationDTO] = inTransaction(organization.lookup(id).map(_.toDTO))

  def insertOrganization(org: OrganizationDTO) = inTransaction{
    organization.insert(org.toOrganization)
  }

  def patch(id: Long, org: OrganizationDTO) = inTransaction{
    update(organization)(or =>
      where(or.id === id)
      set(or.name := org.name)
    ) match {
      case 0 => throw NotFound(s"organization with $id")
      case _ => id
    }
  }

  def sheltersByOrganization(id: Long) = inTransaction{
    from(ShelterSchema.shelter)(shel => where(shel.organization_id === id)
      select(shel)
    ).toList
      .map(_.toDTO)
  }

}
