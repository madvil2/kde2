package animalsDb.action

import animalsDb.dao.FileDAO
import animalsDb.dto.GeneralIdDTO
import io.netty.handler.codec.http.multipart.FileUpload
import xitrum.annotation.Swagger.{Description, Response, Summary}
import xitrum.annotation.{GET, POST, Swagger}

import scala.util.Try

@POST("files")
@Swagger(
  Summary("файлы"),
  Description("создание файла"),
  Response(200, "id файла")
)
class PostFile extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    GeneralIdDTO(
      FileDAO.createFile(paramo[FileUpload]("file")).getOrElse(
        throw new InternalError("file upload error")
      )
    )
  }.toEither
}

@GET("files/:id")
@Swagger(
  Summary("файлы"),
  Description("файл по айди"),
  Response(200, "id файла")
)
class GetFile extends AbstractAction with AuthorizedAction {
  override def perform(): Either[Throwable, AnyRef] = Try{
    respondFile(FileDAO.filepathById(paramo[Long]("id")).getOrElse(throw new Exception))
  }.toEither
}
