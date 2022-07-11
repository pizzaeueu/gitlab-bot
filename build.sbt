import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "3.1.3"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",         // emit warning and location for usages of deprecated APIs
  "-explain",             // explain errors in more detail
  "-explain-types",       // explain type errors in more detail
  "-feature",             // emit warning and location for usages of features that should be imported explicitly
  "-unchecked",           // enable additional warnings where generated code depends on assumptions
)

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .settings(
    libraryDependencies += "dev.zio" %% "zio"                 % "2.0.0-RC2",
    libraryDependencies += "dev.zio" %% "zio-test-sbt"        % "2.0.0-RC2" % Test,
    libraryDependencies += "dev.zio" %% "zio-test"            % "2.0.0-RC2" % Test,
    libraryDependencies += "dev.zio" %% "zio-config"          % "3.0.0-RC3",
    libraryDependencies += "dev.zio" %% "zio-config-magnolia" % "3.0.0-RC3",
    libraryDependencies += "dev.zio" %% "zio-config-typesafe" % "3.0.0-RC3",
    libraryDependencies += "dev.zio" %% "zio-logging"         % "2.0.0-RC5",
    libraryDependencies += "io.d11"  %% "zhttp"               % "2.0.0-RC4",
    libraryDependencies += "dev.zio" %% "zio-json"            % "0.3.0-RC3",
    libraryDependencies += ("com.evolutiongaming" %% "crypto" % "2.0.1").cross(CrossVersion.for3Use2_13),
  )
