package animalsDb.errors

import animalsDb.dto.ErrorDTO
import io.netty.handler.codec.http.HttpResponseStatus

class NotFound(override val message: ErrorDTO,
               override val code: HttpResponseStatus = HttpResponseStatus.NOT_FOUND
              ) extends AbstractThrowable

object NotFound {
  def apply() = new NotFound(ErrorDTO("NotFound"))

  def apply(msg: String) = new NotFound(ErrorDTO(msg))
}
