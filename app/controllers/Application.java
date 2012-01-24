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
      return ok(index.render("2 Enter the name of your new bar:", form(Bar.class)));
   }

   public static Result addBar() {
      Form<Bar> form = form(Bar.class).bindFromRequest();
      if (form.hasErrors()) {
         return badRequest(index.render("Error in Bar form.  Enter new Bar", form(Bar.class)));
      } else {
         Bar bar = form.get();
         bar.save();
         return ok(
                 index.render("2 The bar  " + bar.name + " was saved.   Enter a new Bar:", form(Bar.class))
         );
      }
   }

   public static Result listBars() {
      JsonNode jsonNodes = toJson(Bar.findAll());
      return ok(jsonNodes);
   }
}