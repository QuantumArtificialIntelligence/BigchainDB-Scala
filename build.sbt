import Dependencies._

resolvers += Resolver.mavenLocal // Declares the path to the local Maven, where BigchainDB driver was published

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "ai.quantumintelligence",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "BigchainDBTest",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.authenteq" % "java-bigchaindb-driver" % "0.2.0-SNAPSHOT"
  )

mainClass in Compile := Some("ai.quantumintelligence.bigchain.Main")
