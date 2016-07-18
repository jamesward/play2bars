package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Bar;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

import play.libs.Json;

public class Application extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject WebJarAssets webJarAssets;

    public Result index() {
        return ok(views.html.index.render(formFactory.form(Bar.class), webJarAssets));
    }

    public Result addBar() {
        Form<Bar> form = formFactory.form(Bar.class).bindFromRequest();
        Bar bar = form.get();
        bar.save();
        return redirect(controllers.routes.Application.index());
    }

    public Result listBars() {
        JsonNode jsonNodes = Json.toJson(Bar.find.all());
        return ok(jsonNodes);
    }

}
