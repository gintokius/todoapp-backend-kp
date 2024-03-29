resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "Flyway" at "https://flywaydb.org/repo"
resolvers += "Flyway davidmweber" at "https://davidmweber.github.io/flyway-sbt.repo"


addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.4")
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.11.0")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "5.0.0")
addSbtPlugin("com.github.tototoshi" % "sbt-slick-codegen" % "1.3.0")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "5.0.0")
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.14")

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.7-dmr"