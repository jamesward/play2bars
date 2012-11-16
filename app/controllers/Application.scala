package controllers

import play.api.data.Form
import play.api.data.Forms.{single, text}
import play.api.mvc.{Action, Controller}

import models.Bar
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import play.api.libs.json.Json._
import scala.Some
import play.modules.reactivemongo.ReactiveMongoPlugin

//an implicit Application needs to be in scope to create the mongo db
import play.api.Play.current

//need the implicit reactive mongo reader to query mongo db
import reactivemongo.bson.handlers.DefaultBSONHandlers._

//one more implicit needed, because reactive mongo excecutes in a asnyc fashion
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global


object Application extends Controller {

  val db = ReactiveMongoPlugin.db
  val barsCollection = db("bars")

  def create(bar: Bar) {
    barsCollection.insert[Bar](bar).map(lastError =>
      "Mongo LastErorr:%s".format(lastError))
  }

  val barForm = Form(single("name" -> text))

  def index = Action {
    Ok(views.html.index(barForm))
  }

  def addBar() = Action {
    implicit request =>
      barForm.bindFromRequest.value map {
        name =>
          val bar = new Bar(Some(BSONObjectID.generate), name)
          create(bar)
          Redirect(routes.Application.index())
      } getOrElse BadRequest
  }

  def getBars() = Action {
    Async {

      implicit val reader = Bar.BarBSONReader

      // empty query to match all the documents
      val query = BSONDocument()
      // the future cursor of documents
      val barsCursor = barsCollection.find(query)

      // build (asynchronously) a list containing all the articles
      barsCursor.toList.map {
        barsList =>
          Ok(toJson(barsList))
      }
    }
  }

}