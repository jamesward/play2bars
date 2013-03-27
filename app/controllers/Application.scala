package controllers

import play.api.data.Form
import play.api.data.Forms.{single, nonEmptyText}
import play.api.mvc.{Action, Controller}

import models.Bar
import play.api.libs.json.Json


object Application extends Controller {
  
  implicit val barWrites = Json.writes[Bar]

  val barForm = Form(
    single("name" -> nonEmptyText)
  )

  def index = Action {
    Ok(views.html.index(barForm))
  }

  def addBar() = Action { implicit request =>
    barForm.bindFromRequest.value map { name =>
      Bar.create(Bar(0, name))
      Redirect(routes.Application.index())
    } getOrElse BadRequest
  }

  def getBars() = Action {
    val bars = Bar.findAll()
    Ok(Json.toJson(bars))
  }
  
}