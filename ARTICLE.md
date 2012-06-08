Getting Started with Play 2, Scala and Squeryl
==============================================

Intro to Play 2
---------------

Play 2 is unique among the many web frameworks available today because it was designed with a stateless architecture.  This allows it to be a very lightweight framework that consumes little resources, such as memory and cpu.  Being stateless also allows it to easily scale horizontally, something that is critical in today's cloud architectures.   The other unique feature of the architecture is that it was designed to handle long lived connections, such as websockets and comet, by using a fully asynchronous HTTP programming model.

The other unique feature of Play 2 is that it is completely self-contained, which makes it well suited for deployment to cloud application platforms like Heroku.  That means deployment of Play applications is simple and avoids the typical environment inconsistency issues that we've experienced with container-based approaches.

Many of the newer web frameworks like Ruby on Rails, Grails, and Django are very productive at first because of dynamic typing.  Play provides the same quick iterations and rapid development that dynamic language frameworks have, but preserves the benefits of statically typed languages Java and Scala languages.  This allows the compiler help the developer but not get in the way of productivity.


Intro to Squeryl
----------------

The best technology to use for integrating with a database is heavily debated.  Luckily we have a number of options for data persistence in the Java and Scala ecosystems.  The default database mapping tool that Play 2 uses is Anorm (Anorm is Not an Object Relational Mapper). As the name implies, Anorm does not do automatic mapping between the object and relational models.  Instead the developer writes native SQL and manually maps relational data to objects.  This approach does have advantages, namely the ability to hand tune the raw queries that are executed.  The Play developers argue that SQL is a great DSL for talking to relational databases and abstracting away the SQL layer may cause you to give up a lot of power and flexibility.

An alternative to Anorm for data persistence in Scala is Squeryl.  In contrast to Anorm, Squeryl is more like hibernate and provides object-relational mapping.  Squeryl provides a type-safe DSL for talking to databases.  Squeryl also allows you to explicitly control the granularity of data objects that are retrieved.  This provides an elegant solution to the N+1 problem common with traditional ORM, like Hibernate.

Since Squeryl is not the default persistence library in Play 2 it does require some additional configuration and setup.  Database evolutions must be managed manually and the database connection needs to be created on initial startup.  Transactions must also be explicitly defined when calling the database in the controller.

Setup Squeryl in Play 2
-----------------------

If you don't already have a Play 2 project, then create a new one after installing Play 2:

    play new mysquerylapp

Chose Scala as the language for the project.

The Squeryl library needs to be added to the Play project.  We will also add the Postgres JDBC driver as a dependency because later we will deploy this project on the cloud with Heroku and the default database on Heroku is Postgres.

Edit the `project/Build.scala` file and update the dependencies:

    val appDependencies = Seq(
      "org.squeryl" %% "squeryl" % "0.9.5-2",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
    )


Then if you are using an Eclipse or IntelliJ Play can automatically create the project for you using either:

    play idea

or

    play eclipsify

Note that we created the project files after updating the dependencies, so the projects would be configured with the required libraries.  If the dependencies change in the future, just re-run the commands to create the projects.

You can now start the application from within your project's root directory:

    play ~run

Verify that the server is running by opening the following URL in your browser:  
http://localhost:9000/

For local testing we will use an in-memory "h2" database.  To setup Play to use that database, edit the `conf/application.conf` file and uncomment or add the following lines:

    db.default.driver=org.h2.Driver
    db.default.url="jdbc:h2:mem:play"

The final setup step is to provide Squeryl a database connection but we still want to use the standard Play configuration system to get the database connection information.   This is easily done by adding a `Global` class that can hook into the startup phase of the Play application lifecycle.  Create a new file named `app/Global.scala` containing:

    import org.squeryl.adapters.{H2Adapter, PostgreSqlAdapter}
    import org.squeryl.internals.DatabaseAdapter
    import org.squeryl.{Session, SessionFactory}
    import play.api.db.DB
    import play.api.GlobalSettings
    
    import play.api.Application
    
    object Global extends GlobalSettings {
    
      override def onStart(app: Application) {
        SessionFactory.concreteFactory = app.configuration.getString("db.default.driver") match {
          case Some("org.h2.Driver") => Some(() => getSession(new H2Adapter, app))
          case Some("org.postgresql.Driver") => Some(() => getSession(new PostgreSqlAdapter, app))
          case _ => sys.error("Database driver must be either org.h2.Driver or org.postgresql.Driver")
        }
      }
    
      def getSession(adapter:DatabaseAdapter, app: Application) = Session.create(DB.getConnection()(app), adapter)
    
    }

On application startup the `db.default.driver` configuration parameter will be used to determine which driver to use to setup the database connection.  A new connection will be made and stored in Squeryl's SessionFactory.

If you reload the `localhost:9000` webpage in your browser, everything should still be working and you should see the following message in the Play STDOUT log:

    [info] play - database [default] connected at jdbc:h2:mem:play


Create an Entity
----------------

Now lets create a simple entity object that will be used to persist data into the database.  Create a new file named `app/models/Bar.scala` containing:

    package models
    
    import org.squeryl.{Schema, KeyedEntity}
    
    case class Bar(name: Option[String]) extends KeyedEntity[Long] {
      val id: Long = 0
    }
    
    object AppDB extends Schema {
      val barTable = table[Bar]("bar")
    }

This is a very simple entity that will store a list of `Bar` objects.  Each `Bar` has a name and an `id` property for the primary key.  The case class in Scala is immutable and basically super charges a class adding a number of syntactic conveniences.  It also allows it to be used for pattern matching which can be quite handy when matching form values returned from the client.  The `AppDB` object is an instance of the `Schema` that Squeryl will map into the database.  In this case we are only defining one table that will be called `bar` in the database.  Because the schema is defined as an object, it makes it a singleton instance.

With Squeryl you can programatically create the database schema by calling the `AppDB.create` method.  However, a better approach is to manually create the SQL scripts (what play calls evolutions scripts).  Play will then to track your database schema evolutions by checking the database schema against these SQL scripts.  When Play detects the schema is out of date it, it will suggest applying this SQL script.  It only does this while in DEV mode, in PROD mode it applies the script before starting the application.  This allows us to have full control over the changes in our database schema and version the schema changes in case a rollback needs to happen.

Create a new file named `conf/evolutions/default/1.sql` containing:

    # --- First database schema
    
    # --- !Ups
    
    create sequence s_bar_id;
    
    create table bar (
      id    bigint DEFAULT nextval('s_bar_id'),
      name  varchar(128)
    );
    
    
    # --- !Downs
    
    drop table bar;
    drop sequence s_bar_id;

This simple SQL script has two sections: "Ups" and "Downs".  The "Ups" section brings the database schema "up" to this version.  The "Downs" section takes the database down from this version.  Play will apply the database schema changes in order based on their names.  If you need to change the schema after you've deployed and done an evolution to `1.sql` then you'd create a `2.sql` file containing your changes.

If you reload the `localhost:9000` webpage you will now see that Play is asking you if you want to apply the database evolutions.  Click the `Apply this script now!` button to run the "Ups" for `1.sql` on your local in-memory database.

Test the Model
--------------

The testing support in Play 2 is very powerful and fits well with the Test Driven Development style.  Play 2 with Scala uses [specs2](http://etorreborre.github.com/specs2/) for testing.  Lets create a simple test for the `Bar` model object.  Create a new file named `test/BarSpec.scala` containing.

    import models.{AppDB, Bar}
    
    import org.specs2.mutable._
    import org.squeryl.PrimitiveTypeMode.inTransaction
    
    import play.api.test._
    import play.api.test.Helpers._
    
    class BarSpec extends Specification {
      
      "Bar model" should {
        "be creatable" in {
          running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
            inTransaction {
              val bar = AppDB.barTable insert Bar(Some("foo"))
              bar.id mustNotEqual 0
            }
          }
        }
      }
      
    }

This test uses a `FakeApplication` with an in-memory database to run the test.  By using the `FakeApplication` the Squeryl database connection will be configured using the `Global` object that was created earlier.  The body of the test simply creates a new instance of `Bar` and tests that the `id` is not equal to zero.  This happens in a Squeryl transaction.  Different from Play 1, test are run from the command line using:

    play test

If the tests worked, then you should see the following message in the Play STDOUT log:

    [info] Passed: : Total 1, Failed 0, Errors 0, Passed 1, Skipped 0

If you'd like to have the tests run whenever the source changes then run:

    play ~test

You can keep both the `~run` and `~test` commands running in the background.  This allows you to quickly test the application from both programmatic `specs2` tests and from manual browser tests.


Creating Bars From a Web Form
-----------------------------

Now lets add a basic web UI for creating new `Bar` objects.  Note that this code will not compile until this entire section is completed.

First update the `app/controllers/Application.scala` file to contain:

    package controllers
    
    import play.api.mvc._
    
    import com.codahale.jerkson.Json
    import play.api.data.Form
    import play.api.data.Forms.{mapping, text, optional}
    
    import org.squeryl.PrimitiveTypeMode._
    import models.{AppDB, Bar}
    
    
    object Application extends Controller {
    
      val barForm = Form(
        mapping(
          "name" -> optional(text)
        )(Bar.apply)(Bar.unapply)
      )
    
      def index = Action {
        Ok(views.html.index(barForm))
      }
    
      def addBar = Action { implicit request =>
        barForm.bindFromRequest.value map { bar =>
          inTransaction(AppDB.barTable insert bar)
          Redirect(routes.Application.index())
        } getOrElse BadRequest
      }
      
    }

The `barForm` provides a mapping from a request parameter named `name` to the `name` property on the `Bar` case class (via it's constructor).  The `index` method has been updated to pass an instance of the `barForm` into the `index` template.  We will update that template next.  The `addBar` method binds the request parameters into an object named `bar` then in a transaction the `bar` is inserted into the database.  Because Squeryl is not integrated into the Play Framework, database transactions need to be explicitly started using the `inTransaction` Squeryl function.  Then the user is redirected back to the index page.  If the request parameters could not be mapped to a `Bar` using the `barForm` then a `BadRequest` error is returned.

Now we need to update the `app/views/index.scala.html` template to contain:

    @(form: play.api.data.Form[Bar])
    
    @main("Welcome to Play 2.0") {
    
        @helper.form(action = routes.Application.addBar) {
            @helper.inputText(form("name"))
            <input type="submit"/>
        }
    
    }

The template now takes a `Form[Bar]` parameter which is passed from the `index` method on the `Application` controller.  Then in the body of the template a new HTML form is rendered using Play 2's form helper.  The form contains an HTML field for the `name` and a submit button.  Notice that the action of the form points to the route to the `Application` controller's `addBar` method.

If you look in the console window at this point you will see the error "value addBar is not a member of controllers.ReverseApplication".  This is because the route file is compiled and the view is checked for a valid route.  But we haven't created a route yet, so edit the `conf/routes` file and add a new line with the following:

    POST    /bars                       controllers.Application.addBar

This creates a HTTP route that maps `POST` requests for the `/bars` URL to the `addBar` method.

Now refresh `localhost:9000` in your browser and you should see the very basic form for adding new `Bar` objects.  If successful, after adding a new `Bar` the browser should just redirect back to the index page.

Now that you have it working lets take a look back at the controller code and get a better understanding of how everything works.  To understand what the `addBar` method is doing it is helpful to first understand how the `implicit` keyword informs the compiler where to find the value from the surrounding scope.  In Scala the `implicit` keyword can be used either as an `implicit` function parameter or an `implicit` object conversion.  The two are quite different but both relate to how Scala resolves the definition.  In this case, `implicit` is used when you call one or more functions and need to pass the same value to all functions.  This strategy is useful in constructing APIs so that users do not have to always be explicit about what parameters are used, but rely on default values instead.

In the case of `addBar` we specify request to be `implicit` because the `barForm.bindFromRequest` method takes a` play.api.mvc.Request` parameter that we no longer need to pass explicitly.  For reference, here is the method definition for the `Form.bindToRequest` method:

    def bindFromRequest()(implicit request: play.api.mvc.Request[_]): Form[T] = {...}

The `bindFromRequest` returns a `Form` object.  In the `addBar` method we call the `value` method on that `Form` instance which returns an `Option[Bar]` in this case.  Then calling `map` gets the `Bar` if it could be created from the form mapping, otherwise the `getOrElse` statement returns the `BadRequest` error.  When the `Bar` object can be created it is saved to the database in a transaction.  <todo: explain the squeryl insert>

Now that you have a good understanding of how to map request parameters to objects and save those objects with Squeryl lets write a test for the new `addBar` controller method.


Test Adding Bars
----------------

Create a new test for the `addBar` controller method by creating a new file named `test/ApplicationSpec.scala` containing:

    import controllers.routes
    import models.{AppDB, Bar}
    
    import org.specs2.mutable._
    import org.squeryl.PrimitiveTypeMode.inTransaction
    
    import play.api.test._
    import play.api.test.Helpers._
    
    class ApplicationSpec extends Specification {
    
      "respond to the addBar Action" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          val result = controllers.Application.addBar(FakeRequest().withFormUrlEncodedBody("name" -> "FooBar"))
          status(result) must equalTo(SEE_OTHER)
          redirectLocation(result) must beSome(routes.Application.index.url)
        }
      }
    
    }

This functional test uses a `FakeApplication` with an in-memory database.  The test makes a request to the `addBar` method on the `Application` controller with a form parameter named `name` and a value of `FooBar`.  Since success in this method is simply a redirect to the `index` page the status is checked to be `SEE_OTHER` and the redirect location is checked to be the URL of the `index` page.  Run this test with either `play test` or `play ~test` if you'd like to keep running tests when your code changes.


Get Bars as JSON
----------------

Now lets add a RESTful service to the application that will return all of the `Bar` objects as JSON serialized data.  Start by adding a new method to the `app/controllers/Application.scala` file:

      def getBars = Action {
        val json = inTransaction {
          val bars = from(AppDB.barTable)(barTable =>
            select(barTable)
          )
          Json.generate(bars)
        }
        Ok(json).as(JSON)
      }

The `getBars` method fetches the `Bar` objects from the database using Squeryl and then creates a JSON representation of the list of `Bar` objects and returns the JSON data.

Now add a new route to the `conf/routes` file:

    GET     /bars                       controllers.Application.getBars

This maps `GET` requests for `/bars` to the `getBars` method.

Try this out in your browser by loading:  
http://localhost:9000/bars

You should see a list of the `Bar` objects you've created serialized as JSON.

As mentioned previously, the transaction needs to be explicityl started with the `inTransaction` Squeryl function, even when selecting values from the database.  Then within the bounds of the transaction, all of the `Bar` entities are retrieved from the database.

The syntax of the query demonstrates the power of Squeryl’s type-safe query language and the power of Scala to create DSLs.  The `from` function takes the type-safe reference to the table as the first parameter.  This is like the SQL `from` keyword.  The second parameter is a function that takes the table to query as a parameter and then specifies what to do on that table, in this case a `select`.  The `from` returns an iterable object which is set to the `bars` immutable val.  Then the `Json.generate` method iterates through the `bars` retrieved from the database and returns them.  The `json` val is then returned in a `Ok` (200 response) with the content type set to `application/json` (the value of `JSON`).


Test JSON Service
-----------------

Now lets update the `test/ApplicationSpec.scala` test to have a new test for the JSON service.  Add the following:

      "respond to the getBars Action" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          inTransaction(AppDB.barTable insert Bar(Some("foo")))
          
          val result = controllers.Application.getBars(FakeRequest())
          status(result) must equalTo(OK)
          contentAsString(result) must contain("foo")
        }
      }

Again this functional test uses a `FakeApplication` and an in-memory database.  It then creates a new `Bar` in the database and makes a request to the `getBars` method on the `Application` controller.  The response is tested to be `OK` (HTTP 200 status code) and to contain the name of the `Bar` that was created.  Like before run this test either `play test` or `play ~test` and you should now have three tests that pass.


Display the Bars with CoffeeScript and jQuery
---------------------------------------------

Now that we have a RESTful JSON service to get the list of `Bar` objects, lets use some CoffeeScript and jQuery to fetch and display them on the `index` page.  One of the new features in Play 2 is the asset compiler that can compile CoffeeScript to JavaScript, syntax check JavaScript, minify JavaScript, and compile LESS to CSS.

Create a new file named `app/assets/javascripts/index.coffee` that contains:

    $ ->
      $.get "/bars", (data) ->
        $.each data, (index, item) ->
          $("#bars").append $("<li>").text item.name

This CoffeeScript uses jQuery to make a `get` request to `/bars` and then iterates through each `bar` and adds it to the element on the page with an id of `bars`.  Now lets update the `app/views/index.scala.html` template to load this script and provide the `bars` element on the page.  Add the follow into the top part of the `main` section of the template :

        <script src="@routes.Assets.at("javascripts/index.min.js")" type="text/javascript"></script>    
        <ul id="bars"></ul>

Notice that `src` of the script uses the `routes.Assets.at` function to get the URL to the `javascripts/index.min.js` file.  Yet, that file doesn't exist.  Play's asset compiler knows that it needs to create that minified file from compiling the `index.coffee` file.  Load the [localhost:9000](http://localhost:9000) webpage again, create a new `Bar` and you should see it displayed on the webpage.


Deploy on Heroku
----------------

Heroku is a Polyglot Cloud Application Platform that provides a place to run Play 2 apps on the cloud.  To deploy this application on Heroku, follow these steps:

1. First create a new file in the root directory named `Procfile` that contains:

        web: target/start -Dhttp.port=${PORT} -DapplyEvolutions.default=true -Ddb.default.driver=org.postgresql.Driver -Ddb.default.url=$DATABASE_URL ${JAVA_OPTS}

    That tells Heroku how to start the Play application.

2. Heroku uses Git as the way to transfer files to Heroku.  [Install Git](http://git-scm.com/download You will need to create a new git) if you don't already have it. Then from the root directory of your project create a new Git repository for this project, add your files and commit them:

        git init
        git add .
        git commit -m init

3. The Heroku Toolbelt is a command line interface to Heroku.  [Install the Heroku Toolbelt](http://toolbelt.heroku.com).

4. [Signup for a Heroku account](http://heroku.com/signup).

5. Login to Heroku from the command line:

        heroku login

    This will walk you through setting up an SSH key for Git and associating the key with your Heroku account.

6. Provision a new application on Heroku:

        heroku create --stack cedar

7. Now push this applicaiton to Heroku:

        git push heroku master

    Heroku will build the app with SBT and then run it on a [dyno](https://devcenter.heroku.com/articles/dynos).

8. Open the application, now running on the cloud, in your browser:

        heroku open

Congrats!  Your Play 2 app is now running on the cloud!


Further Learning
----------------

All of the source code for this project can be found on GitHub:  
[https://github.com/jamesward/play2bars/blob/scala-squeryl](https://github.com/jamesward/play2bars/blob/scala-squeryl)

When you local Play web server is running you can access Play's local documentation at:  
[http://localhost:9000/@documentation](http://localhost:9000/@documentation)

You can also find the Play documentation at:  
[http://www.playframework.org/documentation](http://www.playframework.org/documentation)

To learn more about Heroku, visit the Heroku Dev Center:  
[http://devcenter.heroku.com](http://devcenter.heroku.com)

We hope this was helpful, please let us know if you have any questions or problems.