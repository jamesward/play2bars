package models

import play.api.libs.json.Json
import scalikejdbc.WrappedResultSet
import scalikejdbc._
import scalikejdbc.async._
import scalikejdbc.async.FutureImplicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class Bar(id: Long, name: String)

object Bar extends SQLSyntaxSupport[Bar] {

  implicit val jsonFormat = Json.format[Bar]

  override val columnNames = Seq("id", "name")

  lazy val b = Bar.syntax

  def db(b: SyntaxProvider[Bar])(rs: WrappedResultSet): Bar = db(b.resultName)(rs)

  def db(b: ResultName[Bar])(rs: WrappedResultSet): Bar = Bar(
    rs.long(b.id),
    rs.string(b.name)
  )

  def create(name: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Bar] = {
    val sql = withSQL(insert.into(Bar).namedValues(column.name -> name).returningId)
    sql.updateAndReturnGeneratedKey().map(id => Bar(id, name))
  }

  def findAll(implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[List[Bar]] = {
    withSQL(select.from[Bar](Bar as b)).map(Bar.db(b))
  }

}