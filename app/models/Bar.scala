package models

import play.api.db._
import play.api.Play.current
import net.vz.mongodb.jackson.{Id, ObjectId}
import org.codehaus.jackson.annotate.JsonProperty
import play.modules.mongodb.jackson.MongoDB
import reflect.BeanProperty


class Bar(@ObjectId @Id val id: String,
          @BeanProperty @JsonProperty("name") val name: String) {
  @ObjectId @Id def getId = id;
}

object Bar {
  private lazy val db = MongoDB.collection("bars", classOf[Bar], classOf[String])

  def create(bar: Bar) { db.save(bar) }
  def findAll() = { db.find().toArray }

  def apply() = {

  }

  def unapply() = {

  }
}
