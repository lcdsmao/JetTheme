enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

plugins {
    id("de.fayard.refreshVersions") version "0.20.0"
}

include(
  "sample",
  "testfixtures-android",
  "jettheme",
  "jettheme-material"
)
