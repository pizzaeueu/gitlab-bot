package com.github.pizzaeueu.gitlabbot.gitlab.services

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Project, Teammate}
import com.github.pizzaeueu.gitlabbot.domain.{MRInfo, MrState}
import com.github.pizzaeueu.gitlabbot.gitlab.clients.GitLabClient
import com.github.pizzaeueu.gitlabbot.config.Teammate
import com.github.pizzaeueu.gitlabbot.config.Teammate
import com.github.pizzaeueu.gitlabbot.domain.MRInfo
import zio.*

trait GitLabService:
  def getMrsList(): Task[List[MRInfo]]

  def assignToMr(MRInfo: MRInfo, userIds: List[Teammate]): Task[Unit]

case class GitLabServiceLive(appConfig: AppConfig, client: GitLabClient) extends GitLabService:
  override def getMrsList(): Task[List[MRInfo]] =
    val jobs = combinations(appConfig.gitLab.projects, appConfig.team.usernames.map(_.username)).map { case (project, user) =>
      client.loadMrsList(project, user)
    }
    for
      allMrs: List[MRInfo] <- ZIO.collectAllPar(jobs).map(_.flatten)
      filteredMrs = allMrs.filter(predicateFor)
      _ <- ZIO.logDebug(s"Filtered MRs: \n $filteredMrs")
    yield filteredMrs

  override def assignToMr(mrInfo: MRInfo, userIds: List[Teammate]): Task[Unit] = for
    _ <- ZIO.logDebug(s"MrId = ${mrInfo.id} will be assigned to users: $userIds")
    _ <- client.updateMr(mrInfo.iid, mrInfo.projectId, userIds.map(_.gitlabId))
  yield ()

  private def combinations(projects: List[Project], users: List[String]): List[(Project, String)] = for
    project <- projects
    user    <- users
  yield (project, user)

  private def predicateFor(mr: MRInfo) =
    !mr.title.toLowerCase.startsWith("draft") && mr.state == MrState.opened

object GitLabService:
  def live: RLayer[AppConfig & GitLabClient, GitLabService] = ZLayer.fromFunction(GitLabServiceLive.apply)
