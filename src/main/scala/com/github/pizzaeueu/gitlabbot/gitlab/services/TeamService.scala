package com.github.pizzaeueu.gitlabbot.gitlab.services

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Project, TeamConfig, Teammate}
import com.github.pizzaeueu.gitlabbot.gitlab.clients.GitLabClient
import zio.{RLayer, ZLayer}

trait TeamService {
  def getTeamByProjectId(projectId: Int): Option[TeamConfig]

  def getAllMrToLoad: List[(Project, String)]
}

case class TeamServiceLive(appConfig: AppConfig) extends TeamService {

  def getTeamByProjectId(projectId: Int): Option[TeamConfig] = {
    appConfig.team.find(_.projectIds.contains(projectId))
  }

  def getAllMrToLoad: List[(Project, String)] = {
    val allUsers    = appConfig.team.flatMap(_.usernames).map(_.username)
    val allProjects = appConfig.gitLab.projects

    for {
      user    <- allUsers
      project <- allProjects
    } yield (project, user)
  }
}

object TeamService:
  def live: RLayer[AppConfig, TeamService] = ZLayer.fromFunction(TeamServiceLive.apply)
