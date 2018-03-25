name := "Project3"

crossPaths := false
autoScalaLibrary := false
fork := true

////////////////////////////////////////////////////////////////////////////////
// Unit Testing

libraryDependencies += "junit" % "junit" % "4.11" % "test"
libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test->default"


// https://github.com/JetBrains/sbt-structure
resolvers += Resolver.url("jb-bintray", url("http://dl.bintray.com/jetbrains/sbt-plugins"))(Resolver.ivyStylePatterns)

libraryDependencies += "org.jetbrains" %% "sbt-structure-core" % "2017.2"

lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.13.4"
libraryDependencies += scalacheck % Test


////////////////////////////////////////////////////////////////////////////////
// Java options

javaOptions += "-Xmx2G"
