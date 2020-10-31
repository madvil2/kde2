package animals.action.abstractActions

import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus._

trait PostAbstractAction extends AbstractAction {
  override val successCode: HttpResponseStatus = CREATED
}
