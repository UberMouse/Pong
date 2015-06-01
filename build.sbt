//
enablePlugins(ScalaJSPlugin)

name := "Pong"

version := "1.0"

scalaVersion := "2.11.6"

persistLauncher in Compile := true

persistLauncher in Test := false

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "com.softwaremill.quicklens" %%% "quicklens" % "1.3.1",
  "org.scala-js" %%% "scalajs-dom" % "0.8.0"
)
