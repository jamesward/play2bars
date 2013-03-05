package controllers

import scala.collection.JavaConverters._

import play.api.data.Form
import play.api.data.Forms.{single, text}
import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json

import models.{BarCompanion, Bar}
import models.BarCompanion._



object Application extends Controller {

  val barForm = Form(single("name" -> text))

  def index = Action {
    Ok(views.html.index(barForm))
  }

  def addBar() = Action { implicit request =>
    barForm.bindFromRequest.value map { name =>
      BarCompanion.create(new Bar(null, name))
      Redirect(routes.Application.index())
    } getOrElse BadRequest
  }

  def getBars() = Action {
    val bars = BarCompanion.findAll().asScala
    val json = Json.toJson(bars)
    Ok(json).as("application/json")
  }

}