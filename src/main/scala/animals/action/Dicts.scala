package animals.action

import animals.dao.{DictDAO, FileDAO}
import animals.dto.{DictElemDTO, GeneralIdDTO}
import io.netty.handler.codec.http.HttpResponseStatus
import xitrum.annotation.{DELETE, GET, POST, PUT, Swagger}
import xitrum.annotation.Swagger.{Description, IntPath, OptStringBody, OptStringPath, Response, Schemes, StringBody, StringPath, Summary, Tags}
import HttpResponseStatus._
import animals.action.abstractActions.{AbstractAction, AuthorizedAction, DeleteAbstractAction, PostAbstractAction}

import scala.util.Try


@GET("/dicts")
@Swagger(
  Tags("dicts"),
  Summary("Dicts"),
  Description("Получение списка всех словарей"),
  Response(200, "Лист строк-названий словарей, которые можно использовать в dicts/:dictType/values")
)
class GetDicts extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    DictDAO.dictTableList
  }.toEither
}

@GET("dicts/:dictType/values")
@Swagger(
  Tags("dicts"),
  Summary("Dicts"),
  Description("Получение значений лежащих в словаре"),
  StringPath("dictType", "название словаря"),
  OptStringPath("value", "фильтр по значению словаря. Благодаря этому можно узнать id объекта в словаре зная значение и название словаря"),
  Response(200, "Лист объектов")
)
class GetDictValues extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    DictDAO(param("dictType")).dictValues(queryParams)
  }.toEither
}


@POST("dicts/:dictType")
@Swagger(
  Tags("dicts"),
  Summary("Dicts"),
  Description("Создание нового значения в словаре с именем dictType"),
  StringPath("dictType", "название таблицы-словаря"),
  Response(200, "Объект-значение нового словаря")
)
class PostValuesDict extends PostAbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    DictDAO(param("dictType")).insertValue(requestContentJValue.extract[DictElemDTO])
  }.toEither
}

@PUT("dicts/:dictType/:id")
@Swagger(
  Tags("dicts"),
  Summary("Dicts"),
  Description("Создание нового значения в словаре с именем dictType"),
  StringPath("dictType", "название таблицы-словаря"),
  IntPath("id", "айди конкретного словаря"),
  StringBody("value", "значение"),
  OptStringBody("description", "описание"),
  Response(200, "Объект-значение нового словаря")
)
class PutValuesDict extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val dictElem = requestContentJValue.extract[DictElemDTO] match {
      case x if x.id.isDefined => x
      case x => x.copy(id = paramo[Long]("id"))
    }
    DictDAO(param("dictType")).insertValue(dictElem)
  }.toEither
}

@DELETE("dicts/:dictType/:id")
@Swagger(
  Tags("dicts"),
  Summary("Dicts"),
  Description("Удаление значения из словаря"),
  StringPath("dictType", "название таблицы-словаря"),
  IntPath("id", "айди конкретного словаря"),
  Response(200, "Объект-значение нового словаря")
)
class DeleteValueDict extends DeleteAbstractAction with AuthorizedAction{

  override def perform(): Either[Throwable, AnyRef] = Try {
    GeneralIdDTO(
      DictDAO(param("dictType")).deleteById(param[Long]("id"))
    )
  }.toEither
}