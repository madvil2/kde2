package animalsDb.action

import java.nio.charset.StandardCharsets

import org.squeryl.PrimitiveTypeMode
import xitrum.Action

import scala.util.{Failure, Success, Try}
import java.util.Base64

import animalsDb.dao.AccountDAO
import animalsDb.errors.Unauthorized

trait AuthorizedAction extends PrimitiveTypeMode{
  this: Action =>

  lazy val currentAccountId: Long = Try(request.headers().get("auth")) match {
    case Success(res) => {
      val decodedBytes = Base64.getDecoder.decode(res)
      val decodedStr = new String(decodedBytes, StandardCharsets.UTF_8).split(":")
      AccountDAO.setAccountId(decodedStr(0))(decodedStr(1))
    }
    case Failure(_) => throw Unauthorized()
  }

}