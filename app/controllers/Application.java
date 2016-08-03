package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Bar;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.mvc.Controller;
import play.mvc.Result;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import play.libs.Json;

public class Application extends Controller {

    @Inject
    private JPAApi jpaApi;

    @Inject
    private FormFactory formFactory;

    @Inject WebJarAssets webJarAssets;

    public Result index() {
        return ok(views.html.index.render(formFactory.form(Bar.class), webJarAssets));
    }

    @Transactional
    public Result addBar() {
        Form<Bar> form = formFactory.form(Bar.class).bindFromRequest();
        Bar bar = form.get();
        jpaApi.em().persist(bar);
        return redirect(controllers.routes.Application.index());
    }

    @Transactional(readOnly = true)
    public Result listBars() {
        CriteriaBuilder cb = jpaApi.em().getCriteriaBuilder();
        CriteriaQuery<Bar> cq = cb.createQuery(Bar.class);
        Root<Bar> rootEntry = cq.from(Bar.class);
        CriteriaQuery<Bar> all = cq.select(rootEntry);
        TypedQuery<Bar> allQuery = jpaApi.em().createQuery(all);
        JsonNode jsonNodes = Json.toJson(allQuery.getResultList());
        return ok(jsonNodes);
    }

}
