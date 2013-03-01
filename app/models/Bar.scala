package models

import org.squeryl.{Session, Schema, KeyedEntity}
import play.api.libs.json.Json
import org.squeryl.PrimitiveTypeMode._
import com.escalatesoft.subcut.inject.{BindingModule, Injectable}


case class Bar(name: String) extends KeyedEntity[Long] {
  val id: Long = 0
}

object BarCompanion {
  implicit val barReads = Json.reads[Bar]
  implicit val barWrites = Json.writes[Bar]
}

class BarDAO(implicit val bindingModule: BindingModule) extends Injectable {
  
  def getAllBars: List[Bar] = {
    using(inject[Session]) {
      from(AppDB.barTable)(barTable =>
        select(barTable)
      ).toList
    }
  }
  
  def createBar(bar: Bar) {
    using(inject[Session])(AppDB.barTable insert bar)
  }
  
}

object AppDB extends Schema {
  val barTable = table[Bar]("bar")
}