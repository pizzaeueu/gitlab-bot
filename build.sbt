import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "3.1.3"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .settings(
    libraryDependencies += "dev.zio" %% "zio"                 % "2.0.1",
    libraryDependencies += "dev.zio" %% "zio-test-sbt"        % "2.0.1" % Test,
    libraryDependencies += "dev.zio" %% "zio-test"            % "2.0.1" % Test,
    libraryDependencies += "dev.zio" %% "zio-config"          % "3.0.2",
    libraryDependencies += "dev.zio" %% "zio-config-magnolia" % "3.0.2",
    libraryDependencies += "dev.zio" %% "zio-config-typesafe" % "3.0.2",
    libraryDependencies += "dev.zio" %% "zio-logging"         % "2.1.12",
    libraryDependencies += "io.d11"  %% "zhttp"               % "2.0.0-RC10",
    libraryDependencies += "dev.zio" %% "zio-json"            % "0.3.0-RC11",
    libraryDependencies += ("com.evolutiongaming" %% "crypto" % "2.0.1").cross(CrossVersion.for3Use2_13),
  )
