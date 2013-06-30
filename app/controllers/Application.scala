package controllers

import play.api.mvc._

import play.api.data.Form
import play.api.data.Forms.{mapping, text, optional}

import org.squeryl.PrimitiveTypeMode._
import models.{AppDB, Bar}
import play.api.libs.json.Json


object Application extends Controller {

  val barForm = Form(
    mapping(
      "name" -> optional(text)
    )(Bar.apply)(Bar.unapply)
  )

  def index = Action {
    Ok(views.html.index(barForm))
  }

  def getBars = Action {
    implicit val barWrites = Json.writes[Bar]
    val json = inTransaction {
      val bars = from(AppDB.barTable)(barTable =>
        select(barTable)
      )
      Json.toJson(bars)
    }
    Ok(json)
  }

  def addBar = Action { implicit request =>
    barForm.bindFromRequest.value map { bar =>
      inTransaction(AppDB.barTable insert bar)
      Redirect(routes.Application.index())
    } getOrElse BadRequest
  }
  
}