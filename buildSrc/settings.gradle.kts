import de.fayard.refreshVersions.bootstrapRefreshVersionsForBuildSrc
import de.fayard.refreshVersions.migrateRefreshVersionsIfNeeded

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
////                                                    # available:0.10.0")
////                                                    # available:0.10.1")
}

migrateRefreshVersionsIfNeeded("0.9.7") // Will be automatically removed by refreshVersions when upgraded to the latest version.

bootstrapRefreshVersionsForBuildSrc()
