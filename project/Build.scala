import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "foobar"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      resolvers += Resolver.file("localRepo", file("repository/local"))(Resolver.ivyStylePatterns)
    )


}
