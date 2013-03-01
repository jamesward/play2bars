package controllers

import play.api.mvc.{Controller, Action}

import play.api.data.Form
import play.api.data.Forms.{mapping, text, optional}

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Session
import models.{BarDAO, AppDB, Bar}
import com.escalatesoft.subcut.inject.Injectable
import play.api.libs.json.Json
import models.BarCompanion._
import configs.StandardConfig

trait ApplicationBase extends Controller with Injectable {

  lazy val barDao = injectOptional[BarDAO] getOrElse new BarDAO()(bindingModule)
  
  val barForm = Form(
    mapping(
      "name" -> text
    )(Bar.apply)(Bar.unapply)
  )

  def index = Action {
    Ok(views.html.index(barForm))
  }

  def getBars = Action {
    Ok(Json.toJson(barDao.getAllBars)).as(JSON)
  }

  def addBar = Action { implicit request =>
    barForm.bindFromRequest.value map { bar =>
      barDao.createBar(bar)
      Redirect(routes.Application.index())
    } getOrElse BadRequest
  }
  
}

object Application extends ApplicationBase {
  val bindingModule = StandardConfig
}