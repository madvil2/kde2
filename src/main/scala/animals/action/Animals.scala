package animals.action

import animals.action.abstractActions.{AbstractAction, PostAbstractAction}
import xitrum.annotation.{GET, POST, Swagger}
import animals.dao.Animals.AnimalsDAO
import animals.dto.AnimalInputDTO
import animals.schema.AnimalProperties
import xitrum.annotation.Swagger.{Description, Response, Summary}

import scala.util.Try

@POST("/animals/:animalType")
class PostAnimal extends PostAbstractAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val obj = AnimalsDAO(param("animalType"))
    obj.createAnimal(requestContentJValue)
  }.toEither
}

@Swagger(
  Summary("животные"),
  Description("реестр животных"),
  Response(200, "id файла")
)
@GET("/animals/:animalType")
class GetAnimals extends AbstractAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val obj = AnimalsDAO(param("animalType"))
    obj.animals(0, 20, queryParams)
  }.toEither
}

