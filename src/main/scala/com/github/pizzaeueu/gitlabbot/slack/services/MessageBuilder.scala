package com.github.pizzaeueu.gitlabbot.slack.services

import com.github.pizzaeueu.gitlabbot.config.{AppConfig, Teammate}
import com.github.pizzaeueu.gitlabbot.domain.{DividerBlock, MRInfo, MarkdownText, SectionBlock, SlackMessage}
import com.github.pizzaeueu.gitlabbot.domain.*
import com.github.pizzaeueu.gitlabbot.config.Teammate
import com.github.pizzaeueu.gitlabbot.domain.MRInfo
import zio.*

trait MessageBuilder {
  def buildMrAssignmentMessage(assignees: List[(MRInfo, List[Teammate])]): Task[SlackMessage]
}

case class MessageBuilderLive(appConfig: AppConfig) extends MessageBuilder {
  override def buildMrAssignmentMessage(assignees: List[(MRInfo, List[Teammate])]): Task[SlackMessage] = for
    blocks <- ZIO.succeed(
      assignees.flatMap { case (mrInfo, teammates) =>
        List(
          SectionBlock(
            MarkdownText(
              s"""
               | Assigned to: ${teammates.map(_.username).mkString(", ")}
               | Title: `${mrInfo.title}`
               |  <${mrInfo.url}|GitLab>
               | Description:
               | ```
               | ${mrInfo.description}
               | ```
               | Labels: ${mrInfo.labels.map(lbl => s"`$lbl`").mkString(", ")}
               |""".stripMargin
            )
          ),
          DividerBlock,
        )
      }
    )
  yield SlackMessage(blocks, appConfig.slack.channel)
}

object MessageBuilder {
  def live: RLayer[AppConfig, MessageBuilder] = ZLayer.fromFunction(MessageBuilderLive.apply)
}
