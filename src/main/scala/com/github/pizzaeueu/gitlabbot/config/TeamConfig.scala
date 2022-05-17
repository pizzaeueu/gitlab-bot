package com.github.pizzaeueu.gitlabbot.config

case class TeamConfig(
  usernames: List[Teammate]
)

case class Teammate(
  username: String,
  gitlabId: Int,
)
