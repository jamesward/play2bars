import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2bars-java-neo4j"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.neo4j" % "neo4j-rest-graphdb" % "1.8"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      resolvers += "Neo4j" at "http://m2.neo4j.org/content/repositories/releases"
    )

}
