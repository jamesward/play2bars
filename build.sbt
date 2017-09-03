lazy val root = (project in file(".")).enablePlugins(PlayScala)

name := "play2bars-scala-quill"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  evolutions,
  "org.postgresql" % "postgresql" % "42.1.4",

  "io.getquill" %% "quill-async-postgres" % "1.3.0",

  "org.webjars" % "bootstrap" % "3.3.2"
)

pipelineStages := Seq(digest, gzip)
