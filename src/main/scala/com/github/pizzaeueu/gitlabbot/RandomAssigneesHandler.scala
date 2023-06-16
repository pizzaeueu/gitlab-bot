package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Teammate}
import com.github.pizzaeueu.gitlabbot.domain.GitLabUser
import zio.*

import scala.annotation.tailrec
import scala.util

case class RandomAssigneesHandler(ref: Ref[List[Teammate]]) extends AssigneesHandler {

  override def chooseAssignees(teammates: List[Teammate], amount: Int): Task[List[Teammate]] = for
    _ <- ZIO.logInfo("Choosing assigners")
    assigners <- getRandomAssignees(
      teammates,
      amount = amount,
    )
  yield assigners

  private def getRandomAssignees(team: List[Teammate], amount: Int) =
    ZIO.succeed { util.Random.shuffle(team).take(amount) }
}

object RandomAssigneesHandler {
  def randomHandler: URLayer[Ref[List[Teammate]], AssigneesHandler] = ZLayer.fromFunction(RandomAssigneesHandler.apply)
}
