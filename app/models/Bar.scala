package models

import scala.reflect.BeanProperty

import play.api.Play.current
import play.modules.mongodb.jackson.MongoDB
import play.api.libs.json._

import net.vz.mongodb.jackson.{Id, ObjectId}

import org.codehaus.jackson.annotate.JsonProperty


class Bar(@ObjectId @Id val id: String, @BeanProperty @JsonProperty("name") val name: String) {
  @ObjectId
  @Id
  def getId = id;
}

object BarCompanion {
  
  implicit val barReads = new Reads[Bar] {
    def reads(js: JsValue): JsResult[Bar] = JsSuccess(
      new Bar(
        (js \ "id").as[String],
        (js \ "name").as[String]
      )
    )
  }
  
  implicit val barWrites = new Writes[Bar] {
    def writes(bar: Bar): JsValue = {
      Json.obj(
        "name" -> bar.name
      )
    }
  }
  private lazy val db = MongoDB.collection("bars", classOf[Bar], classOf[String])

  def create(bar: Bar) { db.save(bar) }
  def findAll() = { db.find().toArray }
}
