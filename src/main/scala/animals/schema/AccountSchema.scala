package animals.schema

import animals.dto.AccountDTO
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations.Column
import org.squeryl.{KeyedEntity, Schema}
import cats.implicits._

case class Account(id: Long = 0,
                   login: String,
                   username: String,
                   pass: String,
                   @Column("file_id")fileId: Option[Long]
                  ) extends KeyedEntity[Long] {
  def this() = this(0, "", "", "", None)
}

object Account {
  def apply(id: Long = 0, login: String, username: String, pass: String, fileId: Option[Long] = None): Account = {
    new Account(id, login, username, pass, fileId)
  }

  implicit class AccountsToAccountDTO(acc: Account) {
    def toDTO = AccountDTO(acc.id.some, acc.login, acc.username, acc.fileId)
  }
}

object AccountSchema extends Schema{
  val account = table[Account]("account")
}
