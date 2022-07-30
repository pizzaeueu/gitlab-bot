package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Teammate}
import com.github.pizzaeueu.gitlabbot.domain.GitLabUser
import zio.*

import scala.annotation.tailrec
import scala.util

case class RandomAssigneesHandler(appConfig: AppConfig, ref: Ref[List[Teammate]]) extends AssigneesHandler {

  override def chooseAssignees(gitLabUser: GitLabUser): Task[List[Teammate]] = for
    _ <- ZIO.logInfo("Choosing assigners")
    assigners <- ref.updateAndGet(teammates =>
      getRandomAssignees(
        appConfig.team.usernames
          .filterNot(teammates.toSet)
          .filterNot(_.username == gitLabUser.username),
        amount = appConfig.team.amount,
      )
    )
  yield assigners

  private def getRandomAssignees(team: List[Teammate], amount: Int): List[Teammate] =
    util.Random.shuffle(team).take(amount)
}

object RandomAssigneesHandler {
  def randomHandler: URLayer[Ref[List[Teammate]] & AppConfig, AssigneesHandler] = ZLayer.fromFunction(RandomAssigneesHandler.apply)
}
