package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Bar(id: Long, name: String)

object Bar {

  val simple = {
    get[Long]("id") ~
    get[String]("name") map {
      case id~name => Bar(id, name)
    }
  }

  def findAll(): Seq[Bar] = {
    DB.withConnection { implicit connection =>
      SQL("select * from bar").as(Bar.simple *)
    }
  }

  def create(bar: Bar): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into bar(name) values ({name})").on(
        'name -> bar.name
      ).executeUpdate()
    }
  }

}