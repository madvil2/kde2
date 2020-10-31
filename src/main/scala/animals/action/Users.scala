package animals.action

import animals.action.abstractActions.{AbstractAction, PostAbstractAction}
import animals.dao.AccountDAO
import animals.dto.{AccountDTO, AccountPatchDTO, GeneralIdDTO}
import animals.errors.Forbidden
import xitrum.annotation.{GET, PATCH, POST, PUT, Swagger}

import scala.util.Try


@POST("users/login")
class PostUsersLogin extends PostAbstractAction {
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
class PostUsers extends PostAbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    AccountDAO.createAccount(requestContentJValue.extract[AccountDTO])
  }.toEither
}

@PATCH("users/:id")
class PatchUsers extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val id = param[Long]("id")
    if (currentAccountId == id) GeneralIdDTO(AccountDAO.updateAccount(id, requestContentJValue.extract[AccountPatchDTO]))
    else throw Forbidden("You can patch only own settings")
  }.toEither
}