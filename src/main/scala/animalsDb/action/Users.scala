package animalsDb.action

import animalsDb.dao.AccountDAO
import animalsDb.dto.{AccountDTO, GeneralIdDTO}
import xitrum.annotation.Swagger._
import xitrum.annotation.{GET, POST, Swagger}

import scala.util.Try


@POST("users/login")
class PostUsersLogin extends AbstractAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val userId = AccountDAO.accountIdByLogPass(param("login"))(param("password"))
    GeneralIdDTO(userId)
  }.toEither
}

@GET("users")
class GetUsers extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    AccountDAO.accountList(params = queryParams)
  }.toEither
}

@GET("users/:id")
class GetUserById extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    AccountDAO.accountById(param[Long]("id"))
  }.toEither
}

@POST("users")
class PostUsers extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    AccountDAO.createAccount(requestContentJValue.extract[AccountDTO])
  }.toEither
}
