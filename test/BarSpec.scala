import models.{AppDB, Bar}

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.squeryl.PrimitiveTypeMode.inTransaction

import play.api.test._
import play.api.test.Helpers._

class BarSpec extends FlatSpec with ShouldMatchers {
  
  "A Bar" should "be creatable" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      inTransaction {
        val bar = AppDB.barTable insert Bar(Some("foo"))
        bar.id should not equal(0)
      }
    }
  }
  
}
