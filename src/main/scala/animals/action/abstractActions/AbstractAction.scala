package animals.action.abstractActions

import animals.errors.AbstractThrowable
import cats.implicits._
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus._
import org.json4s.DefaultFormats
import xitrum.{ActorAction, SkipCsrfCheck}

abstract class AbstractAction extends ActorAction with SkipCsrfCheck {
  implicit val formats = DefaultFormats

  val successCode: HttpResponseStatus = OK

  def perform(): Either[Throwable, AnyRef]

  def respondError(error: Throwable) {
    def respondCodeAndMessage(message: Option[AnyRef] = None, code: HttpResponseStatus): Unit = {
      response.setStatus(code)
      xitrum.Log.error(error.getStackTrace.toString)
      respondJson(message.getOrElse("error"))
    }

    error match {
      case e => throw e
      case e: AbstractThrowable => respondCodeAndMessage(e.message.some, e.code)
      case e => respondCodeAndMessage(e.getMessage.some, HttpResponseStatus.INTERNAL_SERVER_ERROR)
    }
  }

  override def execute(): Unit = {
    response.headers().set("Access-Control-Allow-Origin", "*")
    response.setStatus(successCode)
    perform() match {
      case Left(e) => respondError(e)
      case Right(data) => respondJson(data)
    }
  }
}
