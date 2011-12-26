import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2bars-scala"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "org.scalaquery" % "scalaquery_2.9.0" % "0.9.4",
      "com.codahale" %% "jerkson" % "0.5.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
      // Add your own project settings here      
    )

}
