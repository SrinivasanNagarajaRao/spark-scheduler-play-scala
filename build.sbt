
name := """smart-insight"""

version := "2.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

// Scala Version Currently been Utilized as Main Source
scalaVersion := "2.11.12"

// Scala Cross Version which are all compatible
crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += guice

// call other HTTP services from within a Play application
libraryDependencies += ws

// Play WS supports HTTP caching, but requires a JSR-107 cache implementation to enable this feature. You can add ehcache:
libraryDependencies += ehcache

libraryDependencies += "com.h2database" % "h2" % "1.4.196"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"

// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test

// Fragile Version Compatible Touch with caution
libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.8.11",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.11",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.8.11",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.11",
  "org.apache.spark" % "spark-core_2.11" % "2.0.0",
  "org.apache.spark" % "spark-mllib_2.11" % "2.0.0",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.3",
  "org.reactivemongo" %% "reactivemongo-play-json" % "0.11.14"
)

libraryDependencies += "com.h2database" % "h2" % "1.4.196" % Test

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.2"
)

//"net.debasishg" %% "redisclient" % "3.4"
libraryDependencies ++= Seq(
  "net.debasishg" %% "redisclient" % "3.5"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// helps to allow Custom Routes
routesGenerator := InjectedRoutesGenerator

javaOptions in Test += "-Dconfig.file=conf/application.conf"
