package controllers;

import models.Bar;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import static libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Enter the name of your new bar:", form(Bar.class)));
    }

    public static Result addBar() {
        Form<Bar> form = form(Bar.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(index.render("Error in Bar form.  Enter new Bar",form(Bar.class)));
        } else {
            Bar bar = form.get();
            bar.save();
            return ok(
                index.render("The bar  " + bar.name + " was saved.   Enter a new Bar:", form(Bar.class))
            );
        }
    }

    public static Result listBars() {
        return ok(toJson(Bar.findAll()));
    }
}