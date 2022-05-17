package com.github.pizzaeueu.gitlabbot.config

final case class AppConfig(
  httpServer: HttpServerConfig,
  slack: SlackConfig,
  gitLab: GitLabConfig,
  team: TeamConfig,
)
