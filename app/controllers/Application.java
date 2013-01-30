package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import services.*;

import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Controller
public class Application extends Controller {

    @Autowired
    private HelloService helloService;

    public Result index() {
        return ok(index.render(helloService.hello()));
    }

}