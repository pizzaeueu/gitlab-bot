package com.github.pizzaeueu.gitlabbot

import zio.*
import zio.config.typesafe.*
import TypesafeConfigSource.*
import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Config, Teammate}
import com.github.pizzaeueu.gitlabbot.gitlab.clients.GitLabClient
import com.github.pizzaeueu.gitlabbot.gitlab.services.GitLabService
import com.github.pizzaeueu.gitlabbot.server.HttpServer
import com.github.pizzaeueu.gitlabbot.server.controllers.JobController
import com.github.pizzaeueu.gitlabbot.slack.clients.SlackClient
import com.github.pizzaeueu.gitlabbot.slack.services.MessageBuilder
import com.github.pizzaeueu.gitlabbot.config.*
import com.github.pizzaeueu.gitlabbot.domain.*
import com.github.pizzaeueu.gitlabbot.config.Teammate
import zio.config.ReadError
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.logging.*
import com.github.pizzaeueu.gitlabbot.domain.SlackMessage.given

import scala.language.postfixOps
import java.io.IOException

object Main extends ZIOAppDefault:

  override def hook: RuntimeConfigAspect = console(
    logLevel = LogLevel.Debug,
    format = LogFormat.bracketed(LogFormat.colored),
  )

  override def run =
    printConfig.provide(Config.live) *>
      HttpServer(_.start).provide(
        JobController.live,
        HttpServer.live,
        Config.live,
        Job.live,
        GitLabClient.live,
        GitLabService.live,
        SlackClient.live,
        ChannelFactory.auto,
        EventLoopGroup.auto(),
        RoundRobinAssigneesHandler.roundRobinHandler,
        ZRef.make[Option[Teammate]](None).toLayer,
        MessageBuilder.live,
      )

  def printConfig: ZIO[AppConfig, IOException, Unit] = for
    _      <- ZIO.logInfo("Starting git lab bot...")
    config <- ZIO.service[AppConfig]
    _      <- ZIO.logInfo(config.toString)
  yield ()
