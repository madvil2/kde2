package animals.dao

import animals.schema.Account
import org.squeryl.{PrimitiveTypeMode, Query}
import animals.schema.AccountSchema._
import animals.dao.AccountDAO.{from, inTransaction, where}
import animals.dto.{AccountDTO, AccountPatchDTO}
import animals.errors.{BadRequest, NotFound}
import com.roundeights.hasher.Implicits._
import xitrum.scope.request.Params

object PassValidate extends PrimitiveTypeMode {
  private val salt = "pepper"

  implicit class PasswordHashed(password: String) {
    def hashed = (password + salt).sha256.hex
  }

  def userIdByLogPass(login: String)(password: String): Long = {
    val passwordHashed = password.hashed
    inTransaction {
      from(account)(acc =>
        where(
          acc.login === login and acc.pass === passwordHashed
        )
          select (acc.id)
      ).headOption.getOrElse(throw NotFound("User not found"))
    }
  }

  def passwordHashed(pass: String): String = pass.hashed
}

object AccountDAO extends PrimitiveTypeMode {

  import PassValidate._

  val accountIdLocal: ThreadLocal[Long] = new ThreadLocal[Long]

  def accountId = accountIdLocal.get()

  def setAccountId(id: Long): Long = {
    accountIdLocal.remove()
    accountIdLocal.set(id)
    id
  }

  def accountIdByLogPass(login: String)(password: String): Long = PassValidate.userIdByLogPass(login)(password)

  def setAccountId(login: String)(password: String): Long = {
    val id = accountIdByLogPass(login)(password)
    setAccountId(id)
    id
  }

  def createAccount(acc: AccountDTO): AccountDTO = inTransaction(account.insert(
    Account(
      0,
      acc.login,
      acc.username,
      acc.password.map(_.hashed).getOrElse(throw BadRequest()),
      acc.fileId
    )
  ).toDTO
  )

  def updateAccount(userId: Long, user: AccountPatchDTO) = inTransaction {
    update(account)(acc => where(userId === acc.id)
      set(acc.pass := user.password.map(_.hashed).getOrElse(acc.pass),
      acc.username := user.username.getOrElse(acc.username),
      acc.fileId := user.fileId.orElse(acc.fileId)
    )) match {
      case 0 => throw NotFound(s"id = $userId")
      case _ => userId
    }
  }

  private def prepareQuery(params: Params): Query[Account] = {
    from(account)(acc => select(acc))
  }

  def accountById(id: Long): AccountDTO = inTransaction {
    account.lookup(id)
      .map(acc => AccountDTO(acc.id, acc.login, acc.username, acc.fileId))
      .getOrElse(throw NotFound(s"user with id = $id"))
  }

  def accountList(params: Params): List[AccountDTO] = inTransaction {
    prepareQuery(params)
      .toList
      .map(acc => AccountDTO(acc.id, acc.login, acc.username, acc.fileId))
  }
}
