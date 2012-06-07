import controllers.routes
import models.{AppDB, Bar}

import org.specs2.mutable._
import org.squeryl.PrimitiveTypeMode.inTransaction

import play.api.http.ContentTypes.JSON
import play.api.test._
import play.api.test.Helpers._

class ApplicationSpec extends Specification {

  "respond to the addBar Action" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val result = controllers.Application.addBar(FakeRequest().withFormUrlEncodedBody("name" -> "FooBar"))
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome(routes.Application.index.url)
    }
  }

  "respond to the getBars Action" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      inTransaction(AppDB.barTable insert Bar(Some("foo")))
      
      val result = controllers.Application.getBars(FakeRequest())
      status(result) must equalTo(OK)
      contentAsString(result) must contain("foo")
    }
  }

}
