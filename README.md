# Git Lab Bot [![Scala CI](https://github.com/pizzaeueu/gitlab-bot/actions/workflows/scala.yml/badge.svg)](https://github.com/pizzaeueu/gitlab-bot/actions/workflows/scala.yml)
Bot which integrates git lab with slack

## Features
- Automatic assign teammates on new merge requests
- Send slack notification for new merge request
- Yet another playgrund with scala / zio ecosystem

## Set Up Standalone 
- Fill [properies](https://github.com/pizzaeueu/gitlab-bot/blob/master/src/main/resources/application.conf) with approperiate values
- `sbt run` will run the project
- Create webhook on gitlab project to notify bot about new MRs -- [webhook url](https://github.com/pizzaeueu/gitlab-bot/blob/master/src/main/scala/com/github/pizzaeueu/gitlabbot/server/controllers/JobController.scala#L15)

## Keywords
Scala, Scala3, zio, zio-config, zio-config-magnolia, zio-config-typesafe, zio-logging, zhttp, zio-http, zio-json, zio-test, zio-test-sbt
