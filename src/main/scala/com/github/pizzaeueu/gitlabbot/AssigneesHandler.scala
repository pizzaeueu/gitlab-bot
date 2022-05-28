package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.Teammate
import com.github.pizzaeueu.gitlabbot.domain.{GitLabUser, MRInfo}
import com.github.pizzaeueu.gitlabbot.config.*
import zio.*

trait AssigneesHandler {
  def chooseAssignees(gitLabUser: GitLabUser): Task[List[Teammate]]
}
