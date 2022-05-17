addCommandAlias(
  "build",
  """|;
     |clean;
     |Test / run;
     |package;
  """.stripMargin,
)

addCommandAlias("fmt", "scalafmtSbt; scalafmtAll;")
