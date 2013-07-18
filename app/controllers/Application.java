package controllers;


import models.Bar;
import org.springframework.beans.factory.annotation.Autowired;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import services.BarService;
import views.html.index;

@org.springframework.stereotype.Controller
public class Application {

    @Autowired
    private BarService barService;

    public Result index() {
        return play.mvc.Controller.ok(index.render(Form.form(Bar.class)));
    }

    public Result addBar() {
        Form<Bar> form = Form.form(Bar.class).bindFromRequest();
		if(form.hasErrors()){
			return play.mvc.Controller.badRequest(index.render(form));
		} else {
			Bar bar = form.get();
			barService.addBar(bar);
			return play.mvc.Controller.redirect(controllers.routes.Application.index());
		}
    }

    public Result listBars() {
        return play.mvc.Controller.ok(Json.toJson(barService.getAllBars()));
    }
    
}