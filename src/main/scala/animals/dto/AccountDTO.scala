package animals.dto

import animals.Schema.Account
import animals.dao.FileDAO
import cats.implicits._

case class AccountDTO(id: Option[Long],
                      login: String,
                      username: String,
                      fileId: Option[Long] = None,
                      pass: Option[String] = None,
                      filepath: Option[String] = None
                     )
object AccountDTO {
  def apply(id: Long, login: String, username: String, fileId: Option[Long]) = {
    new AccountDTO(id.some, login, username, fileId, filepath = FileDAO.filepathById(fileId))
  }
  implicit class AccountDTOtoAccount(acc: AccountDTO) {
    //def toAccount: Account = new Account(acc.id.getOrElse(0), acc.login, acc.username, acc.fileId, acc.pa)
  }
}