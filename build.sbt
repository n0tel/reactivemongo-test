name := "TestDocker"

version := "1.0"

lazy val `testdocker` = (project in file(".")).enablePlugins(PlayScala, DockerPlugin)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

// DOCKER
dockerExposedPorts := Seq(9000)

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.11"
)
