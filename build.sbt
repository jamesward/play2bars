lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

name := "play2bars-java-ebean"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.4.1209",
  "org.webjars" %% "webjars-play" % "2.5.0-2",
  "org.webjars" % "bootstrap" % "2.3.1"
)

pipelineStages := Seq(digest, gzip)
