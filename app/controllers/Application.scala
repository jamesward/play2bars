package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.codahale.jerkson._
import anorm._

import models._
import views._



object Application extends Controller {

  val barForm = Form(
    single("name" -> nonEmptyText)
  )

  def index = Action {
    Ok(views.html.index(barForm))
  }

  def addBar() = Action { implicit request =>
    barForm.bindFromRequest.fold(
      errors => BadRequest,
      {
        case (name) =>
          val bar =  Bar.create(
            Bar(NotAssigned, name)
          )
          
          Logger.info(bar.toString)
          
          Redirect(routes.Application.index())
      }
    )
  }

  def listBars() = Action {
    val bars = Bar.findAll()

    val json = Json.generate(bars)

    Ok(json).as("application/json")
  }

}