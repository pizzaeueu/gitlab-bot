package com.github.pizzaeueu.gitlabbot

import zio.test.*
import zio.*
import Assertion.*
import com.github.pizzaeueu.gitlabbot.config.*
import zio.ZRef

object RoundRobinAssigneesHandlerSpec extends DefaultRunnableSpec {

  private val roundRobinHandlerTestLayer: URLayer[Ref[Option[Teammate]] & AppConfig, RoundRobinAssigneesHandler] =
    (RoundRobinAssigneesHandler.apply _).toLayer

  private val teammates = List(
    Teammate("first", 1),
    Teammate("second", 2),
    Teammate("third", 3),
    Teammate("fourth", 4),
  )

  private val appConfig = AppConfig(
    HttpServerConfig("localhost", 8080),
    SlackConfig(Password("test"), "api.url", "/messagePath", "channel"),
    GitLabConfig("api.url", "/projectPath", "/mergeRequestPath", List.empty),
    TeamConfig(teammates),
  )

  def spec: ZSpec[Environment, Failure] = suite("RoundRobinAssigneesHandler")(
    test("Get next assignees after first teammate") {
      val res = RoundRobinAssigneesHandler(_.chooseAssignees()).provide(
        roundRobinHandlerTestLayer,
        ZLayer.succeed(appConfig),
        ZRef.make[Option[Teammate]](Some(teammates.head)).toLayer,
      )
      assertM(res)(equalTo(List(Teammate("second", 2), Teammate("third", 3))))
    },
    test("Get next assignees after last teammate") {
      val res = RoundRobinAssigneesHandler(_.chooseAssignees()).provide(
        roundRobinHandlerTestLayer,
        ZLayer.succeed(appConfig),
        ZRef.make[Option[Teammate]](Some(teammates.last)).toLayer,
      )
      assertM(res)(equalTo(List(Teammate("first", 1), Teammate("second", 2))))
    },
    test("Get next assignees after pre last teammate") {
      val res = RoundRobinAssigneesHandler(_.chooseAssignees()).provide(
        roundRobinHandlerTestLayer,
        ZLayer.succeed(appConfig),
        ZRef.make[Option[Teammate]](Some(Teammate("third", 3))).toLayer,
      )
      assertM(res)(equalTo(List(Teammate("fourth", 4), Teammate("first", 1))))
    },
  )
}
