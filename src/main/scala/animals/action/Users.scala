package animals.action

import animals.action.abstractActions.{AbstractAction, AuthorizedAction, PostAbstractAction}
import animals.dao.AccountDAO
import animals.dto.{AccountDTO, AccountPatchDTO, GeneralIdDTO}
import animals.errors.Forbidden
import xitrum.annotation.Swagger.{Description, IntQuery, OptStringBody, OptStringQuery, PasswordHeader, Response, StringBody, StringPath, Summary, Tags}
import xitrum.annotation.{GET, PATCH, POST, PUT, Swagger}

import scala.util.Try


@POST("users/login")
@Swagger(
  Tags("users"),
  Summary("юзеры"),
  Description("Basic Auth Login"),
  PasswordHeader("auth", "base64 от login:password"),
  Response(201, "id файла, filename, path")
)
class PostUsersLogin extends PostAbstractAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val userId = AccountDAO.accountIdByLogPass(param("login"))(param("password"))
    GeneralIdDTO(userId)
  }.toEither
}

@GET("users")
@Swagger(
  Tags("users"),
  Summary("юзеры"),
  Description("Получение списка юзеров"),
  OptStringQuery("filterName", "тут будут фильтры"),
  Response(200, "id файла, filename, path")
)
class GetUsers extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    AccountDAO.accountList(params = queryParams)
  }.toEither
}


@GET("users/:id")
@Swagger(
  Tags("users"),
  Summary("юзеры"),
  Description("Получение юзера"),
  IntQuery("id", "получение карточки"),
  Response(200, "id файла, filename, path")
)
class GetUserById extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    AccountDAO.accountById(param[Long]("id"))
  }.toEither
}

@POST("users")
@Swagger(
  Tags("users"),
  Summary("юзеры"),
  Description("Создание юзера"),
  StringBody("login", "логин юзера для входа в систему"),
  StringBody("username", "имя юзера"),
  StringBody("password", "пароль"),
  OptStringBody("fileId", "id файла для аватарки"),
  Response(200, "id файла, filename, path")
)
class PostUsers extends PostAbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    AccountDAO.createAccount(requestContentJValue.extract[AccountDTO])
  }.toEither
}

@PATCH("users/:id")
@Swagger(
  Tags("users"),
  Summary("юзеры"),
  Description("Редактирование юзера"),
  OptStringBody("username", "имя юзера"),
  OptStringBody("password", "пароль"),
  OptStringBody("fileId", "id файла для аватарки"),
  Response(200, "id файла, filename, path")
)
class PatchUsers extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try {
    val id = param[Long]("id")
    if (currentAccountId == id) GeneralIdDTO(AccountDAO.updateAccount(id, requestContentJValue.extract[AccountPatchDTO]))
    else throw Forbidden("You can patch only own settings")
  }.toEither
}