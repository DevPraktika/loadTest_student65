package ru.sremts.load.student65

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import ru.tinkoff.gatling.config.SimulationConfig._
import ru.sremts.load.student65.scenarios._

class Debug extends Simulation {

  // proxy is required on localhost:8888

  setUp(
    HttpScenario().inject(atOnceUsers(1)),
  ).protocols(
    httpProtocol,
  ).maxDuration(testDuration)

}
