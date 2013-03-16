package controllers;


import models.Bar;
import org.codehaus.jackson.JsonNode;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import static play.libs.Json.toJson;

public class Application extends Controller {

   public static Result index() {
      return ok(index.render(Form.form(Bar.class)));
   }

   public static Result addBar() {
      Form<Bar> form = Form.form(Bar.class).bindFromRequest();
      Bar bar = form.get();
      bar.save();
      return redirect(controllers.routes.Application.index());
   }

   public static Result listBars() {
      JsonNode jsonNodes = toJson(Bar.find.all());
      return ok(jsonNodes);
   }
}
