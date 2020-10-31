package animals.dao

import animals.dao.Animals.{AnimalsDAO, DogAnimalDAO}
import animals.dao.OrganizationDAO.{from, inTransaction, where}
import animals.dto.ShelterDTO
import animals.schema.{AnimalSchema, DogProperties, ShelterSchema}
import animals.schema.ShelterSchema._
import org.squeryl.PrimitiveTypeMode
import xitrum.scope.request.Params

object ShelterDAO extends PrimitiveTypeMode {

  def shelterList(offset: Int, limit: Int, params: Params) = inTransaction({
    from(shelter)(org =>
      where(org.id isNotNull)
        select (org)
    )
      .page(offset, limit)
      .toList
      .map(_.toDTO)
  })

  def shelterById(id: Long) = inTransaction {
    shelter.lookup(id).map(_.toDTO)
  }

  def insertShelter(shel: ShelterDTO) = inTransaction(shelter.insert(shel.toShelter))

  def shelterWorkers(id: Long) = inTransaction{
    from(ShelterSchema.shelterWorkers)(
      shel => where(shel.sourceId === id)
        select (shel.targetId)
    ).map(AccountDAO.accountById).toList
  }

  def shelterAnimalsByIdAndKind(id: Long, animalSpecies: String) = inTransaction{
    val obj = AnimalsDAO(animalSpecies)
    from(obj.propsTable)(prop => where(prop.shelter_id === id)
      select(prop.animal_id)
    ).toList
      .map(obj.animalById)
  }
}
