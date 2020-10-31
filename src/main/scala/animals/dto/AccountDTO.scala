package animals.dto

import animals.schema.Account
import animals.dao.FileDAO
import cats.implicits._

case class AccountDTO(id: Option[Long],
                      login: String,
                      username: String,
                      fileId: Option[Long] = None,
                      password: Option[String] = None,
                      filepath: Option[String] = None,
                      file: Option[FileDTO] = None
                     )

object AccountDTO {
  def apply(id: Long, login: String, username: String, fileId: Option[Long]) = {
    new AccountDTO(id.some, login, username, file = fileId.map(id => FileDAO.fileById(id)))
  }

}

case class AccountPatchDTO(username: Option[String],
                           password: Option[String],
                           fileId: Option[Long]
                          )