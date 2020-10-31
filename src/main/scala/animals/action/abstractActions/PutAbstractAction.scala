package animals.action.abstractActions

import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT

trait PutAbstractAction extends AbstractAction {
  override val successCode: HttpResponseStatus = NO_CONTENT
}
