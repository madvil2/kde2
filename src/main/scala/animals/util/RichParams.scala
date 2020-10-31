package animals.util

import java.sql.Timestamp

import xitrum.scope.request.Params
import xitrum.util.DefaultsTo

import scala.reflect.runtime.universe._

object RichParams {

  /** Получение значения параметра из queryParams */
  implicit class ParamByName(queryParams: Params) {
    def byName[T: TypeTag](param: String)(implicit d: T DefaultsTo String) = queryP[T](queryParams, param)
  }

  def queryP[T: TypeTag](queryParams: Params, param: String)(implicit d: T DefaultsTo String): Option[T] =
    queryParams.get(param).map(s => {
      val t = typeOf[T]
      val v = t match {
        case tt if tt <:< typeOf[Char] =>
          s.head(0)
        case tt if tt <:< typeOf[Boolean] =>
          s.head.toBoolean
        case tt if tt <:< typeOf[Int] =>
          s.head.toInt
        case tt if tt <:< typeOf[Long] =>
          s.head.toLong
        case tt if tt <:< typeOf[Timestamp] =>
          new Timestamp(s.head.toLong)
        case tt if tt <:< typeOf[String] =>
          s.head
      }

      v.asInstanceOf[T]
    }
    )

  // TODO: Подумать над необходимостью Option[List[String]], мб можно сделать просто List[String]
  def includedFieldsFromQuery(queryParams: Option[Params]): Option[List[String]] = queryParams match {
    case Some(params) => {
      queryP[String](params, "fieldDetails") match {
        case Some(details) => Some(details.split(",").toList)
        case _ => None
      }
    }
    case _ => None
  }

  def isFieldIncluded(includeFields: Option[List[String]], fieldName: String) = includeFields match {
    case Some(fieldList) => fieldList.contains(fieldName)
    case _ => true
  }

  implicit class ToQueryList(list: List[_]) {
    def toQueryList: String = {
      s"[${list.mkString(", ")}]"
    }
  }

  def toQueryParam(elems: (String, String)*): Params = {
    elems.map {
      case (field: String, value: String) => collection.mutable.Map[String, Seq[String]](field -> Seq(value))
    }.reduce(_ ++ _)
  }
}