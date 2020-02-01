enablePlugins(PlayScala)

name := "play2bars-scalikejdbc-async"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  guice,

  "org.scalikejdbc"       %% "scalikejdbc-async"            % "0.13.0",
  "com.github.jasync-sql" %  "jasync-postgresql"            % "1.0.14",
)

libraryDependencies += "org.webjars" % "bootstrap" % "3.3.2"


pipelineStages := Seq(digest, gzip)
