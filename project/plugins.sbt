resolvers ++= Seq(
    DefaultMavenRepository,
    Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Scala Tools" at "http://scala-tools.org/repo-releases/",
    "sbt-idea-repo" at "http://mpeltonen.github.com/maven/",
    "repo.codahale.com" at "http://repo.codahale.com"
)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "0.11.0")

libraryDependencies += "play" %% "play" % "2.0-beta"
