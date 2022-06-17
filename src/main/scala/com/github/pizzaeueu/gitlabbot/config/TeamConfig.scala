package com.github.pizzaeueu.gitlabbot.config

case class TeamConfig(
  usernames: List[Teammate],
  amount: Int,
)

case class Teammate(
  username: String,
  gitlabId: Int,
)
