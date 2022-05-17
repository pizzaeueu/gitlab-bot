package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Teammate}
import com.github.pizzaeueu.gitlabbot.domain.MRInfo
import com.github.pizzaeueu.gitlabbot.gitlab.services.GitLabService
import com.github.pizzaeueu.gitlabbot.config.*
import zio.*

case class RoundRobinAssigneesHandler(appConfig: AppConfig, ref: Ref[Option[Teammate]]) extends AssigneesHandler {
  override def chooseAssignees(): Task[List[Teammate]] = for
    _       <- ZIO.logInfo("Choosing assigners")
    userOpt <- ref.get
    assigners = userOpt
      .map(user => getNextAssignees(appConfig.team.usernames, user))
      .getOrElse(appConfig.team.usernames.take(2))
    _ <- ref.update(_ => Some(assigners.last))
  yield assigners

  private def getNextAssignees(team: List[Teammate], lastAssignee: Teammate) =
    if (team.last == lastAssignee) {
      team.take(2)
    } else if (team.dropRight(1).last == lastAssignee) {
      List(team.last, team.head)
    } else {
      team.dropWhile(_ != lastAssignee).slice(1, 3)
    }
}

object RoundRobinAssigneesHandler extends Accessible[RoundRobinAssigneesHandler] {
  def roundRobinHandler: URLayer[Ref[Option[Teammate]] & AppConfig, AssigneesHandler] = (RoundRobinAssigneesHandler.apply _).toLayer
}
