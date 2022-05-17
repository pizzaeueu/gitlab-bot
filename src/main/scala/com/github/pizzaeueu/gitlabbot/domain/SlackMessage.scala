package com.github.pizzaeueu.gitlabbot.domain

import zio.json.*
import zio.json.ast.*

enum BlockType:
  case section, divider

enum TextType:
  case mrkdwn extends TextType
  case plain_text extends TextType

sealed abstract class Text(val `type`: TextType) {
  val text: String
}

case class PlainText(text: String)    extends Text(TextType.plain_text)
case class MarkdownText(text: String) extends Text(TextType.mrkdwn)

sealed abstract class Block(val blockType: BlockType)

case object DividerBlock extends Block(BlockType.divider)

case class SectionBlock(text: Text) extends Block(BlockType.section)

case class SlackMessage(blocks: List[Block], channel: String)

object SlackMessage {
  private def textJson(text: Text): Json = {
    Json.Obj(
      "text" -> Json.Obj(
        "type" -> Json.Str(text.`type`.toString),
        "text" -> Json.Str(text.text),
      )
    )
  }

  given JsonEncoder[Block] = JsonEncoder[Json].contramap[Block] {
    case b @ DividerBlock =>
      Json.Obj(
        "type" -> Json.Str(b.blockType.toString)
      )
    case b @ SectionBlock(text) =>
      Json
        .Obj(
          "type" -> Json.Str(b.blockType.toString)
        )
        .merge(textJson(text))
  }
  given JsonEncoder[SlackMessage] = DeriveJsonEncoder.gen[SlackMessage]
}
