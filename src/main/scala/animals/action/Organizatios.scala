package animals.action

import animals.action.abstractActions.{AbstractAction, AuthorizedAction}
import animals.configuration.GlobalConstants.{DefaultLimit, DefaultOffset}
import animals.dao.OrganizationDAO
import animals.dto.OrganizationDTO
import xitrum.annotation.{GET, POST, Swagger}
import xitrum.annotation.Swagger.{Description, LongPath, Response, Summary, Tags}

import scala.util.Try


@GET("/organizations")
@Swagger(
  Tags("organizations"),
  Summary("Эксплуатирующие организации"),
  Description("Реестр организаций"),
  Response(200, "Лист объектов-организаций")
)
class GetOrganizationList extends AbstractAction with AuthorizedAction{
  override def perform(): Either[Throwable, AnyRef] = OrganizationDAO.organizationsList(DefaultOffset, DefaultLimit, queryParams)
}

@POST("/organizations")
@Swagger(
  Tags("organizations"),
  Summary("Эксплуатирующие организации"),
  Description("Создать организацию"),
  Response(201, "Лист объектов-организаций")
)
class PostOrganization extends AbstractAction with AuthorizedAction{
  override def perform(): Either[Throwable, AnyRef] = OrganizationDAO.insertOrganization(requestContentJValue.extract[OrganizationDTO])
}

@GET("/organizations/:id/shelters")
@Swagger(
  Tags("organizations", "shelters"),
  Summary("Список приютов по id организации"),
  LongPath("id", "id организации"),
  Description("Создать организацию"),
  Response(200, "Лист объектов-приютов")
)
class PostOrganization extends AbstractAction with AuthorizedAction{
  override def perform(): Either[Throwable, AnyRef] = Try(
    OrganizationDAO.sheltersByOrganization(param[Long]("id"))
  ).toEither
}