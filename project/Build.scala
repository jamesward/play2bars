import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play2bars-mongodb"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.webjars" % "webjars-play" % "2.1.0",
      "org.webjars" % "jquery" % "1.9.1",
      "net.vz.mongodb.jackson" %% "play-mongo-jackson-mapper" % "1.1.0"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
    )

}
