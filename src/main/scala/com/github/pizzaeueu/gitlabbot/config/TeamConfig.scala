package com.github.pizzaeueu.gitlabbot.config

case class TeamConfig(
  teamName: String,
  usernames: List[Teammate],
  amount: Int,
  projectIds: List[Int],
)

case class Teammate(
  username: String,
  gitlabId: Int,
)
