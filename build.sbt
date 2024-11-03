ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "TemplateBasedGenerator"
  )

libraryDependencies += "org.freemarker" % "freemarker" % "2.3.32"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.4.1"

