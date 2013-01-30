import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play2bars-java-spring"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.springframework" % "spring-context" % "3.2.1.RELEASE"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
