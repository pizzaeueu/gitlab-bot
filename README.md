# Git Lab Bot [![Scala CI](https://github.com/pizzaeueu/gitlab-bot/actions/workflows/scala.yml/badge.svg)](https://github.com/pizzaeueu/gitlab-bot/actions/workflows/scala.yml)  [![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
Bot which integrates git lab with slack

## Features
- Automatic assign teammates on new merge requests
- Send slack notification for new merge request
- Yet another playground with scala / zio ecosystem

## Set Up Standalone 
- Fill [properies](https://github.com/pizzaeueu/gitlab-bot/blob/master/src/main/resources/application.conf) with approperiate values
- `sbt run` will run the project
- Create webhook on gitlab project to notify bot about new MRs -- [webhook url](https://github.com/pizzaeueu/gitlab-bot/blob/master/src/main/scala/com/github/pizzaeueu/gitlabbot/server/controllers/JobController.scala#L15)

## Keywords
Scala, Scala3, zio, zio-config, zio-config-magnolia, zio-config-typesafe, zio-logging, zhttp, zio-http, zio-json, zio-test, zio-test-sbt
