package controllers


import javax.inject.Inject

import models.{Bar, BarDB}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global


class Application @Inject() (barDB: BarDB) extends InjectedController {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def getBars = Action.async {
    barDB.findAll().map { bars =>
      Ok(Json.toJson(bars))
    }
  }

  def createBar = Action.async(parse.formUrlEncoded) { request =>
    barDB.create(request.body("name").head).map { bar =>
      Redirect(routes.Application.index())
    }
  }

}
