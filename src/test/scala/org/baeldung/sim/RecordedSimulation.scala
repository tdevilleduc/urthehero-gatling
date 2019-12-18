package org.baeldung.sim

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import jodd.util.RandomString
import org.slf4j.LoggerFactory

import scala.util.Random

class RecordedSimulation extends Simulation {
  val logger = LoggerFactory.getLogger(classOf[Nothing])

  val httpProtocol = http
    .baseURL("http://145.42.10.73:31075")
    .acceptHeader("application/hal+json")

  val feeder = Iterator.continually(Map("newIsbn" -> (newIsbn()), "newTitle" -> (newTitle())))

  val scn = scenario("RecordedSimulation").feed(feeder)
    .exec(
      http("get_users")
        .get("/api/users")
        .notSilent
        .header("Accept", "application/json")
        .check(status.is(200))
    )

  setUp(scn.inject(constantUsersPerSec(60) during (30))).protocols(httpProtocol)

  def newIsbn() = Random.nextInt(Integer.MAX_VALUE)

  def newTitle() = RandomString.getInstance().randomAlpha(10)

}
