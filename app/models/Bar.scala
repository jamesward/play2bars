package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Bar(id: Pk[Long], name: String, location: Option[String])


object Bar {

  val simple = {
    get[Pk[Long]]("bar.id") ~/
    get[String]("bar.name") ~/
    get[Option[String]]("bar.location") ^^ {
      case id~name~location => Bar(id, name, location)
    }
  }


  def findAll(): Seq[Bar] = {
    DB.withConnection { implicit connection =>
      SQL("select * from bar").as(Bar.simple *)
    }
  }


  def create(bar: Bar): Bar = {
    DB.withTransaction { implicit connection =>

    // Get the project id
      val id: Long = bar.id.getOrElse {
        SQL("select next value for bar_seq").as(scalar[Long])
      }

      // Insert the project
      SQL(
        """
          insert into bar values (
            {id}, {name}, {location}
          )
        """
      ).on(
        'id -> id,
        'name -> bar.name,
        'location -> bar.location
      ).executeUpdate()

      bar.copy(id = Id(id))

    }
  }

}