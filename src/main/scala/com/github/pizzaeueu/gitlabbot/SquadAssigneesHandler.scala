package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Teammate, Unknown}
import com.github.pizzaeueu.gitlabbot.domain.GitLabUser
import zio.{Ref, Task, URLayer, ZIO, ZLayer}

case class SquadAssigneesHandler(appConfig: AppConfig, ref: Ref[List[Teammate]]) extends AssigneesHandler {

  override def chooseAssignees(gitLabUser: GitLabUser): Task[List[Teammate]] = for
    _ <- ZIO.logInfo("Choosing assigners")
    assigners <- ref.updateAndGet(teammates =>
      getSquadAssignees(
        gitLabUser,
        appConfig.team.usernames,
        amount = appConfig.team.amount,
      )
    )
  yield assigners

  private def getSquadAssignees(creator: GitLabUser, team: List[Teammate], amount: Int): List[Teammate] =
    val squad = team.filter(_.username == creator.username).flatMap(_.squad) match
      case ::(head, _) => head
      case Nil         => Unknown

    team.filter(_.squad.contains(squad)).filterNot(_.username == creator.username).take(amount)
}

object SquadAssigneesHandler {
  def randomHandler: URLayer[Ref[List[Teammate]] & AppConfig, AssigneesHandler] = ZLayer.fromFunction(SquadAssigneesHandler.apply)
}
