package controllers

import play.api.data.Form
import play.api.data.Forms.{single, nonEmptyText}
import play.api.mvc.{Action, Controller}
import anorm.NotAssigned
import com.codahale.jerkson.Json

import models.Bar


object Application extends Controller {

  val barForm = Form(
    single("name" -> nonEmptyText)
  )

  def index = Action {
    Ok(views.html.index(barForm))
  }

  def addBar() = Action { implicit request =>
    barForm.bindFromRequest.value map { name =>
      Bar.create(Bar(NotAssigned, name))
      Redirect(routes.Application.index())
    } getOrElse BadRequest
  }

  def listBars() = Action {
    val bars = Bar.findAll()
    val json = Json.generate(bars)
    Ok(json).as(JSON)
  }
  
}