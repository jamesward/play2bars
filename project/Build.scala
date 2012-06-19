import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2bars"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.scalatest" %% "scalatest" % "1.8" % "test",
      "org.squeryl" %% "squeryl" % "0.9.5-2",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "com.jquery" % "jquery" % "1.7.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += "webjars" at "http://webjars.github.com/m2",
      testOptions in Test := Nil
      // Add your own project settings here
    )

}
