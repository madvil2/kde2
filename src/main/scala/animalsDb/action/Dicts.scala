package animalsDb.action

import animalsDb.dao.{DictDAO, FileDAO}
import xitrum.annotation.{GET, Swagger}
import xitrum.annotation.Swagger.{Description, Response, Summary}

import scala.util.Try

@GET("dicts/list")
class GetDicts extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    DictDAO.dictTableList
  }.toEither
}

@GET("dicts/:dict/values")
class GetDictValues extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    DictDAO(param("dict")).dictValues
  }.toEither
}