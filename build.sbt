organization := "holodilnik"
name         := "animals"
version      := "1.0"

scalaVersion := "2.12.10"
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// Xitrum requires Java 8
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

//------------------------------------------------------------------------------

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.4"
libraryDependencies += "org.squeryl" %% "squeryl" % "0.9.9"

//------------------------------------------------------------------------------
libraryDependencies += "tv.cntt" %% "xitrum" % "3.29.0"

// Precompile Scalate templates
import org.fusesource.scalate.ScalatePlugin._
scalateSettings
ScalateKeys.scalateTemplateConfig in Compile := Seq(TemplateConfig(
  (sourceDirectory in Compile).value / "scalate",
  Seq(),
  Seq(Binding("helper", "xitrum.Action", importMembers = true))
))

// Xitrum uses SLF4J, an implementation of SLF4J is needed
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
// For writing condition in logback.xml
libraryDependencies += "org.codehaus.janino" % "janino" % "3.1.0"


//Mine
libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"
libraryDependencies += "com.roundeights" %% "hasher" % "1.2.0"




autoCompilerPlugins := true
addCompilerPlugin("tv.cntt" %% "xgettext" % "1.5.3")
scalacOptions += "-P:xgettext:xitrum.I18n"

// Put config directory in classpath for easier development --------------------

// For "sbt console"
unmanagedClasspath in Compile += baseDirectory.value / "config"

// For "sbt fgRun"
unmanagedClasspath in Runtime += baseDirectory.value / "config"


// Copy these to target/xitrum when sbt xitrum-package is run
XitrumPackage.copy("config", "public", "script")
