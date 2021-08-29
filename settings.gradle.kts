pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    jcenter()
  }
}

plugins {
    id("de.fayard.refreshVersions") version "0.20.0"
}

rootProject.name = "JetTheme"

include(
  "sample",
  "testfixtures-android",
  "jettheme",
  "jettheme-material"
)
