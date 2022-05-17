package com.github.pizzaeueu.gitlabbot

import com.github.pizzaeueu.gitlabbot.config.Teammate
import com.github.pizzaeueu.gitlabbot.domain.MRInfo
import com.github.pizzaeueu.gitlabbot.config.*
import zio.*

trait AssigneesHandler {
  def chooseAssignees(): Task[List[Teammate]]
}
