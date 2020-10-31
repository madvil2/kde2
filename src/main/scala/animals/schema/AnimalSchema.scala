package animals.schema

import animals.dao.Animals.AnimalSpecies.{Cat, Dog}
//import animals.dao.Animals.{AnimalsDAO, AnimalSpecies}
import animals.dao.DictDAO
import animals.dto.{AnimalPropertiesOutputDTO, CatPropertiesOutputDTO, DogPropertiesOutputDTO}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{KeyedEntity, Schema}


case class Animal(id: Long = 0, //TODO переделать в андерскоуп
                  name: String,
                  age: Int,
                  weight: Double,
                  comment: Option[String],
                  species_id: Int,
                  sex_id: Int,
                  color_id: Int,
                  fur_id: Int,
                  ear_id: Int,
                  tail_id: Int,
                  size_id: Int,
                  creator_id: Int,
                  card_number: Option[String],
                  status: Int,
                  file_id: Option[Long]
                 ) extends KeyedEntity[Long] {
  def this() = this(0, "", 0, 0, None, 0, 0, 0, 0, 0, 0, 0, 0, None, 0, None)
}


trait AnimalProperties {
  val animal_id: Long
  def toDTO: AnimalPropertiesOutputDTO
}


case class DogProperties(animal_id: Long, breed_id: Long) extends AnimalProperties {
  def this() = this(0, 0)

  override def toDTO: DogPropertiesOutputDTO = DogPropertiesOutputDTO(
    DictDAO("dict_breed_dog").dictElem(breed_id)
  )

}

case class CatProperties(animal_id: Long, breed_id: Long) extends AnimalProperties {
  def this() = this(0, 0)

  override def toDTO: CatPropertiesOutputDTO = CatPropertiesOutputDTO(
    DictDAO("dict_breed_cat").dictElem(breed_id)
  )
}


object AnimalSchema extends Schema {
  val animals = table[Animal]("animal")
  val dogProperties = table[DogProperties]("property_dog")
  val catProperties = table[CatProperties]("property_cat")
}
