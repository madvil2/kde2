package animalsDb.errors

import animalsDb.dto.ErrorDTO
import io.netty.handler.codec.http.HttpResponseStatus

trait AbstractThrowable extends Exception {
  val message: ErrorDTO
  val code: HttpResponseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR
}