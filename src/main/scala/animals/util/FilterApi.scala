package animals.util

import org.squeryl.dsl.{TOptionLong, TypedExpression}
import xitrum.scope.request.Params
import RichParams._

object FilterApi {

//  implicit class FilterSyntax(params: Params) {
//    def numeric[T, U <: TOptionLong](field: TypedExpression[U, T], filterName: String) = {
//      val op = params.byName(s"${filterName}Op").getOrElse("eq")
//
//      op match {
//        case "eq" => field === params.byName[Long](filterName)
//      }
//    }
//  }
}
