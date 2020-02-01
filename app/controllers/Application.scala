package controllers


import javax.inject.Inject
import models.Bar
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject() extends InjectedController {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def getBars = Action.async {
    Bar.findAll.map { bars =>
      Ok(Json.toJson(bars))
    }
  }

  def createBar = Action.async(parse.formUrlEncoded) { implicit request =>
    Bar.create(request.body("name").head).map { _ =>
      Redirect(routes.Application.index())
    }
  }

}
