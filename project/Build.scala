import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2bars-mongodb"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "com.jquery" % "jquery" % "1.7.1",
      "play.modules.reactivemongo" %% "play2-reactivemongo" % "0.1-SNAPSHOT"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += "webjars" at "http://webjars.github.com/m2",
      resolvers += "sgodbillon" at "https://bitbucket.org/sgodbillon/repository/raw/master/snapshots/"
    )

}
