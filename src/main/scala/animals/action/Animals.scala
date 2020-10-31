package animals.action

import animals.action.abstractActions.{AbstractAction, PostAbstractAction}
import xitrum.annotation.GET
import animals.dao.Animals.AnimalsDAO
import animals.dto.AnimalInputDTO
import animals.schema.AnimalProperties
import xitrum.annotation.POST

import scala.util.Try

@POST("/animals/:animalType")
class PostAnimal extends PostAbstractAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val obj = AnimalsDAO(param("animalType"))
    obj.createAnimal(requestContentJValue)
  }.toEither
}

@GET("/animals/:animalType")
class GetAnimals extends AbstractAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val obj = AnimalsDAO(param("animalType"))
    obj.animals(0, 20, queryParams)
  }.toEither
}

