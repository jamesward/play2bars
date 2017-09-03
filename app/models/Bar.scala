package models

import javax.inject.Inject

import modules.Database
import play.api.libs.json.{Json, Writes}

import scala.concurrent.{ExecutionContext, Future}

case class Bar(id: Long, name: String)

object Bar {
  implicit val jsonWrites: Writes[Bar] = Json.writes[Bar]
}

class BarDB @Inject() (database: Database) (implicit executionContext: ExecutionContext) {

  import database.ctx._

  def findAll(): Future[Seq[Bar]] = {
    run {
      quote {
        query[Bar]
      }
    }
  }

  def create(name: String): Future[Bar] = {
    val bar = Bar(0L, name)

    val queryResult = run {
      query[Bar].insert(lift(bar)).returning(_.id)
    }

    queryResult.map(id => bar.copy(id = id))
  }

}
