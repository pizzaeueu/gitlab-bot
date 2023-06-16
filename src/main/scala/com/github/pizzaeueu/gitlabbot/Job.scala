package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Teammate}
import com.github.pizzaeueu.gitlabbot.domain.MRInfo
import com.github.pizzaeueu.gitlabbot.gitlab.services.{GitLabService, TeamService}
import com.github.pizzaeueu.gitlabbot.slack.clients.SlackClient
import com.github.pizzaeueu.gitlabbot.slack.services.MessageBuilder
import com.github.pizzaeueu.gitlabbot.config.*
import com.github.pizzaeueu.gitlabbot.config.Teammate
import com.github.pizzaeueu.gitlabbot.domain.SlackMessage.given
import zio.*
import zio.json.*

trait Job:
  def assignFreeMrs: Task[Unit]

case class JobLive(
  appConfig: AppConfig,
  slackClient: SlackClient,
  gitLabService: GitLabService,
  assigneesHandler: AssigneesHandler,
  messageBuilder: MessageBuilder,
  teamService: TeamService,
) extends Job:
  override def assignFreeMrs: Task[Unit] = for
    _   <- ZIO.logInfo("Starting job")
    mrs <- gitLabService.getMrsList()
    _   <- ZIO.logDebug(s"loaded mrs: \n $mrs")
    unassignedMrs: List[MRInfo] = mrs.filter { mr => mr.reviewers.isEmpty }
    info    <- ZIO.collectAll(unassignedMrs.map(assign))
    message <- messageBuilder.buildMrAssignmentMessage(info)
    _       <- slackClient.sendMessage(message.toJson)
  yield ()

  private def assign(mrInfo: MRInfo): Task[(MRInfo, List[Teammate])] = for
    team <- ZIO
      .fromOption {
        teamService.getTeamByProjectId(mrInfo.projectId)
      }
      .mapError(_ => new RuntimeException(s"Team wasn't found for project ${mrInfo.projectId}"))
    teamExceptAuthor = team.usernames.filter(_.gitlabId != mrInfo.author.id)
    assignees <- assigneesHandler.chooseAssignees(teamExceptAuthor, team.amount)
    _         <- gitLabService.assignToMr(mrInfo, assignees)
  yield (mrInfo, assignees)

object Job:
  def live: RLayer[AppConfig & SlackClient & GitLabService & AssigneesHandler & MessageBuilder & TeamService, Job] =
    ZLayer.fromFunction(JobLive.apply)
