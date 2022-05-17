package com.github.pizzaeueu.gitlabbot.domain

import zio.json.*

case class GitLabUser(
  id: Int,
  name: String,
  username: String,
)

object GitLabUser:
  given JsonDecoder[GitLabUser] = DeriveJsonDecoder.gen[GitLabUser]
