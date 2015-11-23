name := """trunk"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  specs2 % Test,
  "net.sourceforge.jtds"  %      "jtds"     %        "1.3.1",
  "com.google.code.gson"  %      "gson"     %        "2.4",
  "com.jolbox"            %      "bonecp"   %        "0.8.0-rc3"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
