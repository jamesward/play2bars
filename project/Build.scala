import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2bars-scala"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "net.vz.mongodb.jackson" %% "play-mongo-jackson-mapper" % "1.0.0-rc.3"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
