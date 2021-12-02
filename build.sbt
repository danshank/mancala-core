val scala3Version = "3.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "mancala-core",
    version := "0.1.0",
    description := "A core library for making moves in a manacala game",

    scalaVersion := scala3Version,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",

    scalacOptions ++= Seq(
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials",
      "-language:postfixOps"
    )
  )
