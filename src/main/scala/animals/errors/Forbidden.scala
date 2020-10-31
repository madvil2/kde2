package animals.errors

import animals.dto.ErrorDTO
import io.netty.handler.codec.http.HttpResponseStatus

case class Forbidden(override val message: ErrorDTO,
                     override val code: HttpResponseStatus = HttpResponseStatus.FORBIDDEN) extends AbstractThrowable

object Forbidden {
  def apply() = new Forbidden(ErrorDTO("Forbidden"))

  def apply(msg: String) = new Forbidden(ErrorDTO(msg))

}
