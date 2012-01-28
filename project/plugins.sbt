logLevel := Level.Warn

resolvers ++= Seq(
    DefaultMavenRepository,
    Resolver.url("Play", url("http://playrepository.s3-website-us-east-1.amazonaws.com/repository/local/"))(Resolver.ivyStylePatterns),
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/" ,
    "typesafe-ivy-releases" at "http://repo.typesafe.com/typesafe/ivy-releases/"
)

addSbtPlugin("play" % "sbt-plugin" % "2.0-RC1-SNAPSHOT")

addSbtPlugin("com.typesafe.startscript" % "xsbt-start-script-plugin" % "0.5.0")