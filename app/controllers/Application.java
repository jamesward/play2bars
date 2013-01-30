package controllers;


import models.Bar;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.BarService;
import views.html.index;

import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Controller
public class Application extends Controller {

    @Autowired
    private BarService barService;

    public Result index() {
        return ok(index.render(Form.form(Bar.class)));
    }

    public Result addBar() {
        Form<Bar> form = Form.form(Bar.class).bindFromRequest();
        Bar bar = form.get();
        barService.addBar(bar);
        return redirect(controllers.routes.Application.index());
    }

    public Result listBars() {
        return ok(Json.toJson(barService.getAllBars()));
    }
    
}