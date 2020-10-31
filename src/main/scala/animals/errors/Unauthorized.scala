package animals.errors

import animals.dto.ErrorDTO
import io.netty.handler.codec.http.HttpResponseStatus

case class Unauthorized(override val message: ErrorDTO,
                        override val code: HttpResponseStatus = HttpResponseStatus.UNAUTHORIZED) extends AbstractThrowable

object Unauthorized {
  def apply() = new Unauthorized(ErrorDTO("Unauthorized"))

  def apply(msg: String) = new Unauthorized(ErrorDTO(msg))

}
