import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "3.3.0"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .settings(
    libraryDependencies += "dev.zio"              %% "zio"                 % "2.0.15",
    libraryDependencies += "dev.zio"              %% "zio-test-sbt"        % "2.0.15" % Test,
    libraryDependencies += "dev.zio"              %% "zio-test"            % "2.0.15" % Test,
    libraryDependencies += "dev.zio"              %% "zio-config"          % "3.0.7",
    libraryDependencies += "dev.zio"              %% "zio-config-magnolia" % "3.0.7",
    libraryDependencies += "dev.zio"              %% "zio-config-typesafe" % "3.0.7",
    libraryDependencies += "dev.zio"              %% "zio-logging"         % "2.1.17",
    libraryDependencies += "io.d11"               %% "zhttp"               % "2.0.0-RC11",
    libraryDependencies += "dev.zio"              %% "zio-json"            % "0.5.0",
    libraryDependencies += ("com.evolutiongaming" %% "crypto"              % "2.0.1").cross(CrossVersion.for3Use2_13),
  )
