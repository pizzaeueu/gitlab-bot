package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Teammate}
import com.github.pizzaeueu.gitlabbot.domain.MRInfo
import com.github.pizzaeueu.gitlabbot.gitlab.services.GitLabService
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
) extends Job:
  override def assignFreeMrs: Task[Unit] = for
    _   <- ZIO.logInfo("Starting job")
    mrs <- gitLabService.getMrsList()
    _   <- ZIO.logDebug(s"loaded mrs: \n $mrs")
    unassignedMrs: List[MRInfo] = mrs.filter { mr => mr.assignees.isEmpty }
    info    <- ZIO.collectAll(unassignedMrs.map(assign))
    message <- messageBuilder.buildMrAssignmentMessage(info)
    _       <- slackClient.sendMessage(message.toJson)
  yield ()

  private def assign(mrInfo: MRInfo): Task[(MRInfo, List[Teammate])] = for
    assignees <- assigneesHandler.chooseAssignees()
    _         <- gitLabService.assignToMr(mrInfo, assignees)
  yield (mrInfo, assignees)

object Job extends Accessible[Job]:
  def live: RLayer[AppConfig & SlackClient & GitLabService & AssigneesHandler & MessageBuilder, Job] = (JobLive.apply _).toLayer
