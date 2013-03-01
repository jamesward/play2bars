import com.escalatesoft.subcut.inject.Injectable
import configs.StandardConfig
import models.{BarDAOStandard, AppDB, Bar}

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.squeryl.PrimitiveTypeMode.inTransaction

import play.api.test._
import play.api.test.Helpers._

class BarSpec extends FlatSpec with ShouldMatchers {

  "A Bar" should "be creatable" in new WithApplication {
    implicit val bindingModule = StandardConfig
    val dao = new BarDAOStandard
    dao.createBar(Bar("Fred"))
    dao.createBar(Bar("Domino's"))
    dao.getAllBars.map(_.name) should be (List("Fred", "Domino's"))
  }
  
}
