package com.github.pizzaeueu.gitlabbot.config

case class SlackConfig(
  privateToken: Password,
  apiUrl: String,
  sendMessagePath: String,
  channel: String,
)
