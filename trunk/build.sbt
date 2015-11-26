name := """trunk"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  play.sbt.Play.autoImport.cache,
  specs2 % Test,
  "com.typesafe.play"     %%     "anorm"                    %        "2.4.0",
  "mysql"                 %      "mysql-connector-java"     %        "5.1.37",
  "com.google.code.gson"  %      "gson"                     %        "2.4",
  "jp.t2v"                %%     "play2-auth"               %        "0.14.1",
  "jp.t2v"                %%     "play2-auth-social"        %        "0.14.1",
  "jp.t2v"                %%     "play2-auth-test"          %        "0.14.1" % "test"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
