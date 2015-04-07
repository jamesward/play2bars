package controllers


import models.Bar
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global


object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def getBars = Action.async {
    Bar.findAll.map { bars =>
      Ok(Json.toJson(bars))
    }
  }

  def createBar = Action.async(parse.urlFormEncoded) { request =>
    Bar.create(request.body("name").head).map { bar =>
      Redirect(routes.Application.index())
    }
  }

}
