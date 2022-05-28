package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.*
import com.github.pizzaeueu.gitlabbot.domain.GitLabUser
import zio.test.*
import zio.*
import Assertion.*

object AssigneesHandlerSpec extends DefaultRunnableSpec {

  private val randomAssigneesHandlerTestLayer: URLayer[Ref[List[Teammate]] & AppConfig, RandomAssigneesHandler] =
    (RandomAssigneesHandler.apply _).toLayer

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
    TeamConfig(teammates),
  )

  def spec: ZSpec[Environment, Failure] = suite("RandomAssigneesHandler")(
    test("Check that new and previous assignees are not the same") {
      val prevAssignees = List(Teammate("third", 3), Teammate("fifth", 5))
      val gitlabUser    = GitLabUser(username = "first", id = 1, name = "bob")
      val notAssignees  = prevAssignees :+ Teammate(gitlabUser.username, gitlabUser.id)
      val res = RandomAssigneesHandler(_.chooseAssignees(gitlabUser)).provide(
        randomAssigneesHandlerTestLayer,
        ZLayer.succeed(appConfig),
        ZRef.make[List[Teammate]](notAssignees).toLayer,
      )
      assertM(res)(hasNoneOf(notAssignees))
    }
  )
}
