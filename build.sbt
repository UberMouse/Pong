enablePlugins(ScalaJSPlugin)

name := "Pong"

version := "1.0"

scalaVersion := "2.11.6"

persistLauncher in Compile := true

persistLauncher in Test := false

testFrameworks += new TestFramework("utest.runner.Framework")

libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "utest" % "0.3.1",
  "com.softwaremill.quicklens" %%% "quicklens" % "1.3.1",
  "org.scala-js" %%% "scalajs-dom" % "0.8.0"
)
