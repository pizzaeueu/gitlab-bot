package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.*
import com.github.pizzaeueu.gitlabbot.domain.GitLabUser
import zio.test.*
import zio.{ZIO, *}
import Assertion.*

object AssigneesHandlerSpec extends ZIOSpecDefault {

  private val randomAssigneesHandlerTestLayer: URLayer[Ref[List[Teammate]] & AppConfig, RandomAssigneesHandler] =
    ZLayer.fromFunction(RandomAssigneesHandler.apply)

  private val squadAssigneesHandlerTestLayer: URLayer[Ref[List[Teammate]] & AppConfig, SquadAssigneesHandler] =
    ZLayer.fromFunction(SquadAssigneesHandler.apply)

  private val teammates = List(
    Teammate("first", 1, Some(RiskTeam)),
    Teammate("second", 2, Some(RiskTeam)),
    Teammate("third", 3, Some(OpsTeam)),
    Teammate("fourth", 4, Some(OpsTeam)),
    Teammate("fifth", 5, Some(OpsTeam)),
  )

  private val appConfig = AppConfig(
    HttpServerConfig("localhost", 8080),
    SlackConfig(Password("test"), "api.url", "/messagePath", "channel"),
    GitLabConfig("api.url", "/projectPath", "/mergeRequestPath", List.empty),
    TeamConfig(teammates, 2),
  )

  def spec: Spec[Environment, Any] = suite("RandomAssigneesHandler")(
    test("Check that new and previous assignees are not the same") {
      val prevAssignees = List(Teammate("third", 3, Some(OpsTeam)), Teammate("fifth", 5, Some(OpsTeam)))
      val gitlabUser    = GitLabUser(username = "first", id = 1, name = "bob")
      val notAssignees  = prevAssignees :+ Teammate(gitlabUser.username, gitlabUser.id)
      val res = ZIO
        .serviceWithZIO[RandomAssigneesHandler](_.chooseAssignees(gitlabUser))
        .provide(
          randomAssigneesHandlerTestLayer,
          ZLayer.succeed(appConfig),
          ZLayer.fromZIO(Ref.make[List[Teammate]](notAssignees)),
        )
      assertZIO(res)(hasNoneOf(notAssignees))
    } +
      test("Check squad assignees") {
        val gitlabUser = GitLabUser(username = "third", id = 1, name = "bob")
        val team = teammates
        val res = ZIO
          .serviceWithZIO[SquadAssigneesHandler](_.chooseAssignees(gitlabUser))
          .provide(
            squadAssigneesHandlerTestLayer,
            ZLayer.succeed(appConfig),
            ZLayer.fromZIO(Ref.make[List[Teammate]](team)),
          )
        assertZIO(res)(hasSameElements(List(Teammate("fifth", 5, Some(OpsTeam)), Teammate("fourth", 4, Some(OpsTeam)))))
      }
  )
}
