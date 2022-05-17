package com.github.pizzaeueu.gitlabbot.config

final case class GitLabConfig(
  apiUrl: String,
  projectPath: String,
  mergeRequestPath: String,
  projects: List[Project],
)

final case class Project(
  id: Int,
  token: Password,
)
