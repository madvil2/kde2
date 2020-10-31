package animalsDb.errors


  import animalsDb.dto.ErrorDTO
  import io.netty.handler.codec.http.HttpResponseStatus

  case class BadRequest(override val message: ErrorDTO,
                          override val code: HttpResponseStatus = HttpResponseStatus.BAD_REQUEST) extends AbstractThrowable

  object BadRequest {
    def apply() = new BadRequest(ErrorDTO("BadRequest"))

    def apply(msg: String) = new BadRequest(ErrorDTO(msg))
  }