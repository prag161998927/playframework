package controllers
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import com.sun.tools.javac.main.Arguments
import javax.inject._
import models.TaskListInMemoryModel
import play.api.mvc.{MessagesRequest, _}
import play.api.i18n._
import play.api.data._
import play.api.mvc
import play.filters.csrf._
import play.filters.csrf.CSRF.Token
import play.api.mvc.Flash
class Application @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {


  def login = Action {implicit request: MessagesRequest[AnyContent]  =>
    Ok(views.html.login())
  }


  def validateLoginPost = Action { implicit request=>
    val postvals =  request.body.asFormUrlEncoded
    postvals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.validateUser(username, password)) {
        Redirect(routes.Application.index()).withSession("username" -> username)
      }else {


        Redirect(routes.Application.login()).flashing("error" -> "Invalid username/password combination.")
      }
    }.getOrElse(Redirect(routes.Application.login()))


  }
  def createUser = Action {implicit request=>
    val postvals =  request.body.asFormUrlEncoded
    postvals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.createUser(username, password)) {
        Redirect(routes.Application.index()).withSession("username" -> username)
      }else {


        Redirect(routes.Application.login()).flashing("error" -> "User creation failed.")
      }
    }.getOrElse(Redirect(routes.Application.login()))


  }
  def logout = Action {implicit request: MessagesRequest[AnyContent] =>
    Redirect(routes.Application.login()).withNewSession
  }

    def index = Action { implicit request: MessagesRequest[AnyContent] =>
      Ok(views.html.lineChart(Messages("subheader.time_line")))
    }

    def lineChart = Action { implicit request =>
      Ok(views.html.lineChart(Messages("subheader.time_line")))
    }

    def columnAndBarChart = Action { implicit request =>
      Ok(views.html.columnAndBar(Messages("subheader.bar_column")))
    }

    def areaAndPieChart = Action { implicit request =>
      Ok(views.html.pieAndArea(Messages("subheader.area_pie")))
    }

    def bubbleChart = Action { implicit request =>
      Ok(views.html.bubble(Messages("subheader.bubble")))
    }

  def Homepage = Action { implicit request: MessagesRequest[AnyContent]  =>
    Ok(views.html.page())
  }


}
