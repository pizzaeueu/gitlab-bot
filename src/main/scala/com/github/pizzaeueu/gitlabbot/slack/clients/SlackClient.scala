package com.github.pizzaeueu.gitlabbot.slack.clients

import com.github.pizzaeueu.gitlabbot.config.AppConfig
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.*
import com.github.pizzaeueu.gitlabbot.config.*
import zhttp.http.{Headers, HttpData, Method}

trait SlackClient:
  def sendMessage(message: String): Task[Unit]

case class SlackClientLive(appConfig: AppConfig, factory: ChannelFactory, eventLoopGroup: EventLoopGroup) extends SlackClient:
  import appConfig.slack.*
  override def sendMessage(message: String): Task[Unit] = for
    _ <- ZIO.logInfo("Sending message to slack")
    url = s"$apiUrl$sendMessagePath"
    headers = Headers(
      "Authorization" -> s"Bearer ${privateToken.value}",
      "Content-Type"  -> "application/json",
    )
    res <- Client
      .request(
        url = url,
        headers = headers,
        method = Method.POST,
        content = HttpData.fromString(message),
      )
      .provide(
        ZLayer.succeed(factory),
        ZLayer.succeed(eventLoopGroup),
      )
    out <- res.bodyAsString
    _   <- ZIO.logInfo(s"Message Sent response: $out")
    _   <- ZIO.logInfo("Message was successfully sent")
  yield ()

object SlackClient:
  def live: RLayer[AppConfig & ChannelFactory & EventLoopGroup, SlackClient] = ZLayer.fromFunction(SlackClientLive.apply)
