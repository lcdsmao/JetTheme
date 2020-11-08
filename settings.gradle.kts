import de.fayard.refreshVersions.bootstrapRefreshVersions

pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    jcenter()
  }
}

buildscript {
  repositories { gradlePluginPortal() }
  dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
}

bootstrapRefreshVersions()

rootProject.name = "ThemeAmbient"

include(
  "sample",
  "jettheme",
  "jettheme-material"
)
