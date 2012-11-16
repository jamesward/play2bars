package models

import reactivemongo.bson.handlers.{BSONWriter, BSONReader}
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import reactivemongo.bson.BSONString
import play.api.libs.json.{Json, Writes}

case class Bar(id: Option[BSONObjectID], name: String) {
}

object Bar {

  def barToMap(bar: Bar) = (
    Map(
      "name" -> bar.name
    )
    )

  implicit object BarListWrites extends Writes[List[Bar]] {
    def writes(barList: List[Bar]) = Json.toJson(
      barList.map{
        bar =>
          Map("name" -> bar.name)
      }
    )
  }

  implicit object BarBSONReader extends BSONReader[Bar] {
    def fromBSON(document: BSONDocument): Bar = {
      val doc = document.toTraversable
      Bar(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("name").get.value)
    }
  }

  implicit object BarBSONWriter extends BSONWriter[Bar] {
    def toBSON(bar: Bar) = {
      BSONDocument(
        "_id" -> bar.id.getOrElse(BSONObjectID.generate),
        "name" -> BSONString(bar.name))
    }
  }

}
