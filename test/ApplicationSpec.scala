import com.escalatesoft.subcut.inject.BindingModule
import configs.StandardConfig
import controllers.{ApplicationBase, routes}
import models.{BarDAO, AppDB, Bar}

import org.mockito.ArgumentMatcher
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

import play.api.http.ContentTypes.JSON
import play.api.test._
import play.api.test.Helpers._

class ApplicationSpec extends FlatSpec with MockitoSugar with ShouldMatchers {

  "An add and get through the DAO" should "call add once and get once" in {
    StandardConfig.modifyBindings { implicit module =>
      val theTestDAO = mock[BarDAO]
      module.bind [BarDAO] toSingle theTestDAO

      when(theTestDAO.getAllBars).thenReturn(List(Bar("Bruce's")))

      val application = new TestApplication

      application.addBar(FakeRequest().withFormUrlEncodedBody("name" -> "Bruce's"))
      val getResult = application.getBars(FakeRequest())

      contentAsString(getResult) should include("Bruce's")

      import org.mockito.Matchers._
      verify(theTestDAO).createBar(argThat(new ArgumentMatcher[Bar] {
        def matches(obj: Any): Boolean = obj match {
          case bar: Bar => bar.name == "Bruce's"
        }
      }))

      verify(theTestDAO).getAllBars
    }
  }

  class TestApplication(implicit val bindingModule: BindingModule) extends ApplicationBase
}