package animals.action

import animals.dao.{DictDAO, FileDAO}
import animals.dto.{DictElemDTO, GeneralIdDTO}
import io.netty.handler.codec.http.HttpResponseStatus
import xitrum.annotation.{DELETE, GET, POST, PUT, Swagger}
import xitrum.annotation.Swagger.{Description, Response, Summary}
import HttpResponseStatus._
import animals.action.abstractActions.{AbstractAction, AuthorizedAction, DeleteAbstractAction, PostAbstractAction}

import scala.util.Try

@GET("dicts/list")
class GetDicts extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    DictDAO.dictTableList
  }.toEither
}

@GET("dicts/:dictType/values")
class GetDictValues extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    DictDAO(param("dictType")).dictValues
  }.toEither
}

@POST("dicts/:dictType")
class PostValuesDict extends PostAbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    DictDAO(param("dictType")).insertValue(requestContentJValue.extract[DictElemDTO])
  }.toEither
}

@PUT("dicts/:dictType/:id")
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
class DeleteValueDict extends DeleteAbstractAction with AuthorizedAction{

  override def perform(): Either[Throwable, AnyRef] = Try {
    GeneralIdDTO(
      DictDAO(param("dictType")).deleteById(param[Long]("id"))
    )
  }.toEither
}