package animals.action

import animals.action.abstractActions.{AbstractAction, AuthorizedAction, PostAbstractAction}
import animals.dao.FileDAO
import animals.dto.{ErrorDTO, GeneralIdDTO}
import animals.errors.BadRequest
import io.netty.handler.codec.http.multipart.FileUpload
import xitrum.annotation.Swagger.{Description, FileForm, IntPath, IntQuery, Response, Summary, Tags}
import xitrum.annotation.{GET, POST, Swagger}

import scala.util.Try

@POST("files")
@Swagger(
  Tags("files"),
  Summary("файлы"),
  Description("создание файла"),
  FileForm("file", "form-data"),
  Response(200, "id файла")
)
class PostFile extends PostAbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    GeneralIdDTO(
      FileDAO.createFile(paramo[FileUpload]("file")).getOrElse(
        throw new BadRequest(ErrorDTO("file upload error"))
      )
    )
  }.toEither
}

@GET("files/:id")
@Swagger(
  Tags("files"),
  Summary("файлы"),
  IntPath("id", "id файла"),
  Description("файл по айди"),
  Response(200, "id файла")
)
class GetFile extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    respondFile(FileDAO.filepathById(paramo[Long]("id")).getOrElse(throw new Exception))
  }.toEither
}

@GET("files/:id/info")
@Swagger(
  Tags("files"),
  Summary("файлы"),
  IntPath("id", "id файла"),
  Description("файл-инфо по айди"),
  Response(200, "id файла, filename, path")
)
class GetFileInfo extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    FileDAO.fileById(param[Long]("id"))
  }.toEither
}
