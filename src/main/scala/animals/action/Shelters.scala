package animals.action

import animals.action.abstractActions.{AbstractAction, AuthorizedAction, PostAbstractAction}
import animals.configuration.GlobalConstants.{DefaultLimit, DefaultOffset}
import animals.dao.ShelterDAO
import animals.dto.ShelterDTO
import animals.errors.NotFound
import xitrum.annotation.{GET, POST, Swagger}
import xitrum.annotation.Swagger.{Description, LongBody, LongPath, OptLongBody, Response, StringBody, StringPath, Summary, Tags}

import scala.util.Try

@GET("/shelters")
@Swagger(
  Tags("shelters"),
  Summary("Приюты"),
  Description("Реестр приютов"),
  Response(200, "Лист объектов-приютов")
)
class GetShelters extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    ShelterDAO.shelterList(DefaultOffset, DefaultLimit, queryParams)
  }.toEither
}

@GET("/shelters/:id/workers")
@Swagger(
  Tags("shelters", "users"),
  Summary("Работники приюта"),
  LongPath("id", "id шелтера"),
  Description("Получение реестра приютов с фильтрацией"),
  Response(200, "Лист объектов-юзеров")
)
class GetSheltersWorkers extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    ShelterDAO.shelterWorkers(param[Long]("id"))
  }.toEither
}


@POST("/shelters")
@Swagger(
  Tags("shelters"),
  Summary("Создание приюта"),
  StringBody("address", "адрес приюта"),
  OptLongBody("fileId", "ссылка на файл"),
  LongBody("bossId", "ссылка на босса"),
  Description("Получение реестра приютов с фильтрацией"),
  Response(200, "Лист объектов-юзеров")
)
class PostShelter extends PostAbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    ShelterDAO.insertShelter(requestContentJValue.extract[ShelterDTO])
  }.toEither
}

@GET("/shelters/:id")
@Swagger(
  Tags("shelters"),
  Summary("Информация о приюте"),
  LongPath("id", "id шелтера"),
  Description("Получение реестра приютов с фильтрацией"),
  Response(200, "Лист объектов-юзеров")
)
class GetShelter extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    val id = param[Long]("id")
    ShelterDAO.shelterById(id).getOrElse(throw NotFound(s"shelter: ${id}"))
  }.toEither
}

@GET("/shelters/:id/animals/:animalType")
@Swagger(
  Tags("shelters", "animals"),
  Summary("Животные, проживающие в приюте"),
  LongPath("id", "id шелтера"),
  StringPath("animalType", "Вид животного"),
  Description("Получение реестра животных в приюте"),
  Response(200, "Лист объектов-животных")
)
class GetAnimalsInShelter extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    val id = param[Long]("id")
    val animalType = param("animalType")
    ShelterDAO.shelterAnimalsByIdAndKind(id, animalType)
  }.toEither
}