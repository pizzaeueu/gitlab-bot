package com.github.pizzaeueu.gitlabbot.domain

import zio.json.{DeriveJsonDecoder, JsonDecoder}
import scala.util.Try

enum MrState:
  case opened, closed, locked, merged

object MrState:
  given JsonDecoder[MrState] = JsonDecoder[String].mapOrFail { json => Try(MrState.valueOf(json)).toEither.left.map(_.getMessage) }
