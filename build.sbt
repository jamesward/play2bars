lazy val root = (project in file(".")).enablePlugins(PlayJava)

name := "play2bars-java-jpa"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  javaJpa,
  "org.postgresql" % "postgresql" % "9.4.1209",
  "org.hibernate" % "hibernate-entitymanager" % "5.2.1.Final",
  "dom4j" % "dom4j" % "1.6.1", // https://stackoverflow.com/questions/38278199/play-framework-inject-error
  "org.webjars" %% "webjars-play" % "2.5.0-2",
  "org.webjars" % "bootstrap" % "2.3.1"
)

pipelineStages := Seq(digest, gzip)
