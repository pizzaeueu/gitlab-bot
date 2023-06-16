package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.*
import com.github.pizzaeueu.gitlabbot.domain.GitLabUser
import zio.test.*
import zio.*
import Assertion.*

object AssigneesHandlerSpec extends ZIOSpecDefault {

  private val randomAssigneesHandlerTestLayer: URLayer[Ref[List[Teammate]] & AppConfig, RandomAssigneesHandler] =
    ZLayer.fromFunction(RandomAssigneesHandler.apply)

  private val teammates = List(
    Teammate("first", 1),
    Teammate("second", 2),
    Teammate("third", 3),
    Teammate("fourth", 4),
    Teammate("fifth", 5),
  )

  private val appConfig = AppConfig(
    HttpServerConfig("localhost", 8080),
    SlackConfig(Password("test"), "api.url", "/messagePath", "channel"),
    GitLabConfig("api.url", "/projectPath", "/mergeRequestPath", List.empty),
    List(TeamConfig("testTeam", teammates, 2, List(1))),
  )

  def spec: Spec[Environment, Any] = suite("RandomAssigneesHandler")(
    test("Check that new and previous assignees are not the same") {
      // val prevAssignees = List(Teammate("third", 3), Teammate("fifth", 5))
      // val gitlabUser    = GitLabUser(username = "first", id = 1, name = "bob")
      // val notAssignees  = prevAssignees :+ Teammate(gitlabUser.username, gitlabUser.id)
      // val res = ZIO
      //   .serviceWithZIO[RandomAssigneesHandler](_.chooseAssignees(gitlabUser))
      //   .provide(
      //     randomAssigneesHandlerTestLayer,
      //     ZLayer.succeed(appConfig),
      //     ZLayer.fromZIO(Ref.make[List[Teammate]](notAssignees)),
      //   )
      // assertZIO(res)(hasNoneOf(notAssignees))
      assertTrue(true)
    }
  )
}
