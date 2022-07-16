package com.github.pizzaeueu.gitlabbot.server

import com.github.pizzaeueu.gitlabbot.config.AppConfig
import com.github.pizzaeueu.gitlabbot.server.controllers.JobController
import zhttp.http.*
import zhttp.service.{EventLoopGroup, Server}
import zhttp.service.server.ServerChannelFactory
import zio.*

trait HttpServer {
  def start: Task[ExitCode]
}

case class ServerLive(appConfig: AppConfig, mergeRequestController: JobController) extends HttpServer {
  override def start: Task[ExitCode] =
    Server(mergeRequestController.build())
      .withPort(appConfig.httpServer.port)
      //.withMaxRequestSize(10000)
      //.make
      //.flatMap(start => ZManaged.succeed(println(s"Server started on port: ${start.port}")))
      //.useForever
      .start
      .forever
      .provideSomeLayer(EventLoopGroup.auto(0) ++ ServerChannelFactory.auto)
}

object HttpServer {
  def live: RLayer[AppConfig & JobController, HttpServer] = ZLayer.fromFunction(ServerLive.apply)
}
