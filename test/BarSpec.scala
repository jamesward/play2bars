import models.{AppDB, Bar}

import org.specs2.mutable._
import org.squeryl.PrimitiveTypeMode.inTransaction

import play.api.test._
import play.api.test.Helpers._

class BarSpec extends Specification {
  
  "Bar model" should {
    "be creatable" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        inTransaction {
          val bar = AppDB.barTable insert Bar(Some("foo"))
          bar.id mustNotEqual 0
        }
      }
    }
  }
  
}
