package ru.sremts.load.student65.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import ru.sremts.load.student65.cases._

object HttpScenario {
  def apply(): ScenarioBuilder = new HttpScenario().scn
}

class HttpScenario {

  val scn: ScenarioBuilder = scenario("Http Scenario")
    .exec(HttpActions.getWeatherForecast)
    .exec(HttpActions.getForecast)
    .exec(HttpActions.getCities)
    .exec(HttpActions.getCitiesId)

}
