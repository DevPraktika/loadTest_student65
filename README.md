# Gatling Load Testing Project

У нас нет статистики запросов к нашему API, в базе еще нет реальных данных. Благодаря Swagger известно, что имеется endpoint-ы: `Cities`, `Forecast`, `WeatherForecast`.
Будем считать, что пользователи создают примерно одинаковую нагрузку на все эти endpoint-ы. У будущих операторов сервиса запросили информацию о частоте внесения данных о наблюдаемой погоде, список городов, в которых проводятся наблюдения. И пока нет ответа, проведем нагрузочное тестирование на текущих тестовых данных, ограничившись GET запросами.
В результате у нас получились следующие запросы:

    GET /WeatherForecast
    GET /Forecast
    GET /Cities
    GET /Cities/1

Для нагрузочного тестирования будем использовать инструмент Gatling. И воспользуемся удобным шаблоном `Gatling-template.g8` для быстрого создания проекта тестирования производительности.
Наши запросы описываем в файле [HttpActions.scala](https://github.com/DevPraktika/loadTest_student65/blob/main/src/test/scala/ru/sremts/load/student65/cases/HttpActions.scala). В файле [HttpScenario.scala](https://github.com/DevPraktika/loadTest_student65/blob/main/src/test/scala/ru/sremts/load/student65/scenarios/HttpScenario.scala) сценарий выполнения запросов, так как мы предполагаем примерно одинаковую нагрузку на endpoint-ы, сценарий состоит из последовательно выполняемых запросов.
Настройки параметров http протокола, такие как `virtualHost`, `disableCaching`, `shareConnections` задаем в файле [student65.scala](https://github.com/DevPraktika/loadTest_student65/blob/main/src/test/scala/ru/sremts/load/student65/student65.scala).
В файле [simulation.conf](https://github.com/DevPraktika/loadTest_student65/blob/main/src/test/resources/simulation.conf) задаем параметры теста производительности. Для теста максимальной производительности (MaxPerformance), когда выполняется несколько шагов и на каждом шаге увеличивается интенсивность, зададим:

    rampDuration: 60s # Время разгона
    stageDuration: 300s # Длительность "полки"
    intensity: 100 # Интенсивность
    stagesNumber: 10 # Количество шагов

После завершения теста MaxPerformance получаем отчет https://devpraktika.github.io/loadTest_student65/target/gatling/maxperformance-20231126110159934/index.html
Примем SLA: время отклика - 1 сек, процент ошибок  - 1%.
Анализируя этот отчет и данные мониторинга http://5eca9364-3899-4021-b861-fd4f64e48c6d.mts-gslb.ru/d/WMN8AfnSz/four-golden-signals?orgId=1&from=1700996386852&to=1701000467854 получаем, что после 240req/s API начинает сбоить, не удовлетворяя SLA. Упирается в лимиты выделенные подам (100m CPU, 128Mb RAM), что приводит к перезапуску подов.
Считаем, что максимальная производительность нашего API 80% от полученных 240req/s, т.е. 190req/s.
Теперь проведем тест стабильности (Stability), нагрузив на 80% от полученной максимальной производительности. В файле [simulation.conf](https://github.com/DevPraktika/loadTest_student65/blob/main/src/test/resources/simulation.conf) задаем параметры:

    rampDuration: 10m  # Время разгона
    stageDuration: 50m  # Длительность теста
    intensity: 35 # Интенсивность

После завершения теста Stability получаем отчет в файле https://devpraktika.github.io/loadTest_student65/target/gatling/stability-20231126124615747/index.html
Данные мониторинга http://5eca9364-3899-4021-b861-fd4f64e48c6d.mts-gslb.ru/d/WMN8AfnSz/four-golden-signals?orgId=1&from=1701002697057&to=1701006507496

Вывод. Максимальная производительность - 190req/s, тест стабильности при нагрузке 140req/s прошел успешно. В нашем случае узкое место в ресурсах выделенных подам. Для реализации возможности обрабатывать больше запросов в первую очередь нужно увеличить лимит CPU и RAM для подов. Сейчас кластер БД справляется с нагрузкой, но после увеличения ресурсов подов может потребоваться его оптимизация. Дальше, уже совместно с разработчиками, оптимизировать запросы API к базе и схему базы, добавить ключи для "cityId", "dateTime".  Запрос `WeatherForecast` порождает множество обращений к базе, сначала получая список всех городов, а затем для каждого города все элементы погоды. В большинстве случаев пользователю не будет нужны все элементы погоды, логично ввести ограничения по дате.

