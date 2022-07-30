package com.github.pizzaeueu.gitlabbot.server.controllers

import com.github.pizzaeueu.gitlabbot.Job
import com.github.pizzaeueu.gitlabbot.config.*
import zhttp.http.*
import zhttp.service.Server
import zio.*

trait JobController {
  def build(): HttpApp[Any, Nothing]
}

case class JobControllerLiveControllerLive(job: Job) extends JobController {
  val server: HttpApp[Any, Nothing] = Http.collectZIO[Request] { case Method.POST -> !! / "gitlab-bot" / "job" / "run-apply-assignees" =>
    for
      _ <- ZIO.logDebug("run-apply-assignees was started through API...")
      _ <- job.assignFreeMrs.forkDaemon
    yield Response.status(Status.Ok)
  }

  override def build(): HttpApp[Any, Nothing] = server
}

object JobController {
  def live: RLayer[Job, JobController] = ZLayer.fromFunction(JobControllerLiveControllerLive.apply _)
}
