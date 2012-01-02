import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "playbars2"
  val appVersion = "1.0"

  val jacksonCoreAsl = "org.codehaus.jackson" % "jackson-core-asl" % "1.9.2"
  val jacksonMapperAsl = "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.2"
  val postgresql = "postgresql" % "postgresql" % "9.0-801.jdbc3"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jacksonCoreAsl, jacksonMapperAsl, postgresql
  )

  val main = PlayProject(appName, appVersion, appDependencies).settings(defaultJavaSettings: _*).settings(
    // Add your own project settings here
  )

}
