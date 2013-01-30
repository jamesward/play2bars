import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import configs.AppConfig;
import configs.DataConfig;
import org.codehaus.jackson.JsonNode;
import org.junit.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


@ContextConfiguration(classes={AppConfig.class, DataConfig.class,})
public class ApplicationTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
    
    /*
    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Your new application is ready.");
    }
    */
  
   
}
