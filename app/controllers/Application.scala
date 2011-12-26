package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import format.Formats._

import anorm._

import com.codahale.jerkson.Json._

import models.Bar

object Application extends Controller {

  val barForm = Form(
    of(Bar.apply _)(
      "id" -> ignored(NotAssigned),
      "name" -> requiredText,
      "location" -> optional(text)
    )
  )

  def index = Action {
    Ok(views.html.index(barForm))
  }

  def addBar() = Action { implicit request =>
    barForm.bindFromRequest.fold(
      errors => BadRequest,
      bar => {
          Bar.create(bar)
          Redirect(routes.Application.index())
      }
    )
  }

  def listBars() = Action {
    val bars = Bar.findAll()

    val json = generate(bars)

    Ok(json).as("application/json")
  }
  
}