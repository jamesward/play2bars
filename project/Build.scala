import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play2bars-scala-subcut-squeryl"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.scalatest" %% "scalatest" % "1.9.1" % "test",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "com.escalatesoft.subcut" %% "subcut" % "2.0-SNAPSHOT",
    "org.squeryl" %% "squeryl" % "0.9.5-6",
    "org.webjars" % "webjars-play" % "2.1.0",
    "org.webjars" % "bootstrap" % "2.3.1",
    jdbc
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    testOptions in Test := Nil,
    resolvers += "Maven Central Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  )

}
