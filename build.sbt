name := "rock-paper-scissor"
scalaVersion := "2.12.2"
resolvers += "buildo at bintray" at "https://dl.bintray.com/buildo/maven"
libraryDependencies += "io.buildo" %% "enumero" % "1.2.1"
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)