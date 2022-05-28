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
          .filterNot(_.username == gitLabUser.username)
      )
    )
  yield assigners

  private def getRandomAssignees(team: List[Teammate]): List[Teammate] =
    util.Random.shuffle(team).take(2)
}

object RandomAssigneesHandler extends Accessible[RandomAssigneesHandler] {
  def randomHandler: URLayer[Ref[List[Teammate]] & AppConfig, AssigneesHandler] = (RandomAssigneesHandler.apply _).toLayer
}
