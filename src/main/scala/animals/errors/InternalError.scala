package animals.errors

import animals.dto.ErrorDTO
import io.netty.handler.codec.http.HttpResponseStatus

class InternalError (override val message: ErrorDTO,
                     override val code: HttpResponseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR
                    ) extends AbstractThrowable
