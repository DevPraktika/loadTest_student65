package ru.sremts.load.student65.cases

import io.gatling.http.Predef._
import io.gatling.core.Predef._

object HttpActions {

  val getWeatherForecast = http("GET /WeatherForecast")
    .get("/WeatherForecast")
    .check(status is 200)

  val getForecast = http("GET /Forecast")
    .get("/Forecast")
    .check(status is 200)

  val getCities = http("GET /Cities")
    .get("/Cities")
    .check(status is 200)

  val getCitiesId = http("GET /CitiesId")
    .get("/Cities/1")
    .check(status is 200)
    .check(jsonPath("$.name").is("Rostov-on-Don"))


}
