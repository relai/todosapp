name := """todosapp-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJpa,
  javaWs,
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "org.webjars" % "webjars-play_2.10" % "2.3.0-3",
  "sample.todosapp" % "todosapp-sharedresources" % "1.0-SNAPSHOT"
)

resolvers += (
    "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"
)

fork in run := false