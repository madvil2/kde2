package animals.action

import animals.action.abstractActions.{AbstractAction, PostAbstractAction}
import xitrum.annotation.{GET, POST, Swagger}
import animals.dao.Animals.AnimalsDAO
import animals.dto.{AnimalInputDTO, AnimalOutputDTO}
import xitrum.annotation.Swagger.{Description, DoubleBody, IntBody, OptIntBody, OptStringBody, OptStringPath, Response, StringBody, StringPath, Summary, Tags}
import animals.configuration.GlobalConstants._
import scala.util.Try

@POST("/animals/:animalType")
@Swagger(
  Tags("animals"),
  Summary("животные"),
  Description("Создание животного"),
  StringBody("name", "кличка"),
  IntBody("age", "возраст животного"),
  IntBody("status", "статус готовности животного: -1 недоступно, 0 пока не доступно, доступна"),
  DoubleBody("weight", "вес"),
  IntBody("sexId", "id значения слова dict_sex"),
  IntBody("speciesId", "id значения слова dict_species"),
  IntBody("colorId", "id значения слова dict_color"),
  IntBody("furId", "id значения слова dict_fur"),
  IntBody("tailId", "id значения слова dict_tail"),
  IntBody("sizeId", "id значения слова dict_size"),
  IntBody("creatorId", "id создателя записи"),
  OptStringBody("cardNumber", "номер каточки"),
  OptIntBody("fileId", "id файла для аватарки"),
  StringBody("properties", """Объект вида properties{
                            breed_id: Int,
                            shelter_id: Long,
                            worker_id: Long
                            }
    breed_id айди из словаря dict_breed_cats
    shelter_id - ссылка на приют
    worker_id - на сотрудника приюта
    """),
  Response(200, "id файла, filename, path")
)
class PostAnimal extends PostAbstractAction {
  override def perform(): Either[Throwable, AnimalOutputDTO[_]] = Try {
    val obj = AnimalsDAO(param("animalType"))
    obj.createAnimal(requestContentJValue)
  }.toEither
}

@GET("/animals/:animalType")
@Swagger(
  Tags("animals"),
  Summary("животные"),
  Description("реестр животных по видам"),
  StringPath("animalType", "тип животного, значения dog и cat"),
  OptStringPath("filterName", "название фильтра, который скоро будет"),
  OptStringPath("orderField", "Поле для сортировки"),
  OptStringPath("orderType", "Тип сортировки, asc и desc"),
  Response(200, "Реестр животных")
)
class GetAnimals extends AbstractAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val obj = AnimalsDAO(param("animalType"))
    obj.animals(DefaultOffset, DefaultLimit, queryParams)
  }.toEither
}

