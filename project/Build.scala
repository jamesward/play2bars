import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play2bars-scala-anorm"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    anorm,
    "postgresql" % "postgresql" % "9.0-801.jdbc3",
    "org.webjars" % "webjars-play" % "2.1.0-1",
    "org.webjars" % "bootstrap" % "2.3.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
