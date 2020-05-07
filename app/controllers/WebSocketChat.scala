package controllers

import actors.{ChatActor, ChatManager}
import javax.inject._
import models.TaskListInMemoryModel
import play.api.mvc._
import play.api.i18n._
import models._
import play.api.libs.streams.ActorFlow
import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.Materializer

@Singleton
class WebSocketChat @Inject() (mcc: MessagesControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends MessagesAbstractController(mcc) {
  val manager = system.actorOf(Props[ChatManager], "Manager")

  def index = Action { implicit request =>
    Ok(views.html.chatPage())
  }

  def socket() = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      ChatActor.props(out, manager)
    }
  }
}


























