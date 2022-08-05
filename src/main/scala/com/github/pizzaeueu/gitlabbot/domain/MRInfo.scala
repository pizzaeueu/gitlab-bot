package com.github.pizzaeueu.gitlabbot.domain

import zio.json.*

case class MRInfo(
  iid: Int,
  id: Int,
  state: MrState,
  title: String,
  description: String,
  reviewers: List[GitLabUser],
  assignees: List[GitLabUser],
  author: GitLabUser,
  @jsonField("project_id")
  projectId: Int,
  upvotes: Int,
  @jsonField("source_branch")
  sourceBranch: String,
  @jsonField("merge_status")
  mergeStatus: String,
  @jsonField("has_conflicts")
  hasConflicts: Boolean,
  @jsonField("web_url")
  url: String,
  labels: List[String],
)

object MRInfo:
  given JsonDecoder[MRInfo] = DeriveJsonDecoder.gen[MRInfo]
