package com.github.pizzaeueu.gitlabbot.gitlab.clients

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Project}
import com.github.pizzaeueu.gitlabbot.domain.MRInfo
import com.github.pizzaeueu.gitlabbot.config.*
import MRInfo.*
import com.github.pizzaeueu.gitlabbot.config.AppConfig
import zhttp.http.Headers
import zhttp.http.Method
import zhttp.http.HttpData
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.*
import zio.json.*

trait GitLabClient:
  def loadMrsList(project: Project, user: String): Task[List[MRInfo]]

  def updateMr(mrInfo: Int, projectId: Int, userIds: List[Int]): Task[Unit]

case class GitLabClientLive(appConfig: AppConfig, factory: ChannelFactory, eventLoopGroup: EventLoopGroup) extends GitLabClient:

  override def loadMrsList(project: Project, user: String): Task[List[MRInfo]] =
    import appConfig.gitLab.*
    for
    _ <- ZIO.logDebug(s"Loading Mrs list for projectId: ${project.id}, user: $user")
    headers = Headers(
      "private-token" -> project.token.value
    )
    url = s"$apiUrl$projectPath/${project.id}$mergeRequestPath?author_username=$user&state=opened"
    res <- Client
      .request(url = url, headers = headers)
      .provide(
        ZLayer.succeed(factory),
        ZLayer.succeed(eventLoopGroup),
      )
    dataEither <- res.bodyAsString.map(_.fromJson[List[MRInfo]])
    data <- ZIO.fromEither(dataEither).mapError(err => new RuntimeException(err))
    _ <- ZIO.logInfo(s"Mrs list successfully loaded")
  yield data

  override def updateMr(mrInfoId: Int, projectId: Int, userIds: List[Int]): Task[Unit] =
    import appConfig.gitLab.*
    for
    _ <- ZIO.logInfo(s"Updating Mr: $mrInfoId ")
    url = s"$apiUrl$projectPath/$projectId$mergeRequestPath/$mrInfoId"
    project <- ZIO.fromOption(projects.find(_.id == projectId)).mapError(_ => new RuntimeException(s"project wasn't found for id = $projectId"))
    headers = Headers(
      "private-token" -> project.token.value,
      "Content-Type"  -> "application/json",
    )
    _ <- ZIO.logInfo(url)
    ids = userIds.map(id => s"\"$id\"").mkString(",")
    reqContent = HttpData.fromString(s"{\"assignee_ids\": [$ids]}")
    res <- Client
      .request(
        url = url,
        headers = headers,
        method = Method.PUT,
        content = reqContent
      )
      .provide(
        ZLayer.succeed(factory),
        ZLayer.succeed(eventLoopGroup),
      )
    _ <- ZIO.logDebug(s"Request: url: $url, content: $reqContent")
    strBody <- res.bodyAsString
    _ <- ZIO.logInfo(s"Mr update result: $strBody")
  yield()

object GitLabClient:
  def live: RLayer[AppConfig & ChannelFactory & EventLoopGroup, GitLabClient] = ZLayer.fromFunction(GitLabClientLive.apply)
