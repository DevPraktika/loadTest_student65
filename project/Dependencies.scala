import sbt._

object Dependencies {
  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts",
    "io.gatling"            % "gatling-test-framework",
  ).map(_ % "3.8.4" % Test)

  lazy val gelf: Seq[ModuleID]             = Seq("de.siegmar" % "logback-gelf" % "4.0.2")
  lazy val gatlingPicatinny: Seq[ModuleID] = Seq("ru.tinkoff" %% "gatling-picatinny" % "0.11.2")
  lazy val janino: Seq[ModuleID]           = Seq("org.codehaus.janino" % "janino" % "3.1.8")
}
