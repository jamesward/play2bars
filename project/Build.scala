import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play2bars-java-spring"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      javaCore,
      "com.h2database" % "h2" % "1.3.168",
      "org.springframework" % "spring-context" % "3.2.1.RELEASE",
      "org.springframework" % "spring-orm" % "3.2.1.RELEASE",
      "org.springframework" % "spring-jdbc" % "3.2.1.RELEASE",
      "org.springframework" % "spring-tx" % "3.2.1.RELEASE",
      "org.hibernate" % "hibernate-entitymanager" % "4.1.9.Final",
      "cglib" % "cglib" % "2.2.2"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
