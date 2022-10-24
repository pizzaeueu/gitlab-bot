package com.github.pizzaeueu.gitlabbot.config

case class TeamConfig(
  usernames: List[Teammate],
  amount: Int,
)

case class Teammate(
  username: String,
  gitlabId: Int,
  squad: Option[Squad] = None,
)

sealed trait Squad

case object OpsTeam extends Squad
case object RiskTeam extends Squad

case object Unknown extends Squad
