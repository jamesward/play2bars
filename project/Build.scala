import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play2bars"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      jdbc,
      "org.squeryl" %% "squeryl" % "0.9.5-6",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "org.webjars" %% "webjars-play" % "2.1.0-2",
      "org.webjars" % "jquery" % "1.7.2",
      "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      testOptions in Test := Nil
      // Add your own project settings here
    )

}
