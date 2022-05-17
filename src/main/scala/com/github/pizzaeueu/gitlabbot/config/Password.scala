package com.github.pizzaeueu.gitlabbot.config

import zio.config.magnolia.describe
import zio.config.magnolia.*
import zio.config.ConfigDescriptor
import zio.config.ConfigDescriptor.*
import com.evolutiongaming.crypto.Crypto

@describe("Password consists of application secret and string encrypted with evo crypto library")
case class Password(value: String) extends AnyVal:
  override def toString: String = "***"

object Password:
  val passwordConfigDescriptor: ConfigDescriptor[Password] = Descriptor[Map[String, String]]
    .transform[Password](
      (map: Map[String, String]) =>
        val secret    = map("secret")
        val encrypted = map("encrypted")
        val decrypt   = Crypto.decryptAES(encrypted, secret)
        Password(decrypt)
      ,
      _ => Map.empty,
    ) ?? "Encrypted Password"

  given Descriptor[Password] = Descriptor[Password](passwordConfigDescriptor, None)
