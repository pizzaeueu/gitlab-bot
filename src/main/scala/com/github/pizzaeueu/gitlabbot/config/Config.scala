package com.github.pizzaeueu.gitlabbot.config

import Password.*
import com.typesafe.config.ConfigFactory
import zio.config.*
import zio.config.magnolia.{Descriptor, descriptor}
import zio.config.typesafe.TypesafeConfig
import zio.{IO, Layer, ZIO}

import scala.concurrent.duration.{Duration, FiniteDuration}

val teammateConfigDescriptor = descriptor[Teammate].mapKey(toKebabCase)
val gitlabConfigDescriptor   = descriptor[GitLabConfig].mapKey(toKebabCase)
val slackConfigDescriptor    = descriptor[SlackConfig].mapKey(toKebabCase)
val appConfigDescriptor      = descriptor[AppConfig].mapKey(toKebabCase)

object Config:
  def live: Layer[ReadError[String], AppConfig] = {
    TypesafeConfig.fromTypesafeConfig[AppConfig](ZIO.succeed(ConfigFactory.load()), appConfigDescriptor)
  }
