package animals.dto

import animals.schema.{Animal, AnimalProperties, CatProperties, DogProperties}


trait AnimalPropsInput {
  def toAnimal(animalId: Long): AnimalProperties
}

case class DogPropsInputDTO(breed_id: Long) extends AnimalPropsInput {
  override def toAnimal(animalId: Long): DogProperties = DogProperties(animalId, breed_id)
}

case class CatPropsInputDTO(breed_id: Long) extends AnimalPropsInput {
  override def toAnimal(animalId: Long): CatProperties = CatProperties(animalId, breed_id)
}

case class AnimalInputDTO[T <: AnimalPropsInput](id: Option[Long] = None,
                                                 name: String,
                                                 age: Int,
                                                 weight: Double,
                                                 comment: Option[String],
                                                 speciesId: Int,
                                                 sexId: Int,
                                                 colorId: Int,
                                                 furId: Int,
                                                 earId: Int,
                                                 tailId: Int,
                                                 sizeId: Int,
                                                 creatorId: Int,
                                                 cardNumber: Option[String],
                                                 status: Int,
                                                 fileId: Option[Long],
                                                 properties: T
                                                )

object AnimalInputDTO {

  implicit class ToAnimal(animal: AnimalInputDTO[_ <: AnimalPropsInput]) {

    import animal._

    def toAnimal = Animal(id.getOrElse(0),
      name,
      age,
      weight,
      comment,
      speciesId,
      sexId,
      colorId,
      furId,
      earId,
      tailId,
      sizeId,
      creatorId,
      cardNumber,
      status,
      fileId
    )
  }

}


trait AnimalPropertiesOutputDTO

case class DogPropertiesOutputDTO(breed: DictElemDTO) extends AnimalPropertiesOutputDTO

case class CatPropertiesOutputDTO(breed: DictElemDTO) extends AnimalPropertiesOutputDTO

case class AnimalOutputDTO[T <: AnimalPropertiesOutputDTO](id: Long,
                                                           name: String,
                                                           age: Int,
                                                           weight: Double,
                                                           comment: Option[String],
                                                           speciesObj: DictElemDTO,
                                                           sexObj: DictElemDTO,
                                                           colorObj: DictElemDTO,
                                                           furObj: DictElemDTO,
                                                           earObj: DictElemDTO,
                                                           tailObj: DictElemDTO,
                                                           sizeObj: DictElemDTO,
                                                           creatorObj: AccountDTO,
                                                           cardNumber: Option[String],
                                                           status: Int,
                                                           file: Option[FileDTO],
                                                           properties: T
                                                          )