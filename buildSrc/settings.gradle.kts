pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
  plugins {
      id("de.fayard.refreshVersions") version "0.20.0"
////                              # available:"0.21.0"
////                              # available:"0.22.0"
////                              # available:"0.23.0"
////                              # available:"0.30.0"
////                              # available:"0.30.1"
////                              # available:"0.30.2"
////                              # available:"0.40.0"
////                              # available:"0.40.1"
////                              # available:"0.40.2"
////                              # available:"0.50.0"
////                              # available:"0.50.1"
////                              # available:"0.50.2"
////                              # available:"0.51.0"
////                              # available:"0.60.0"
////                              # available:"0.60.1"
////                              # available:"0.60.2"
////                              # available:"0.60.3"
////                              # available:"0.60.4"
////                              # available:"0.60.5"
  }
}

buildscript {
  repositories { gradlePluginPortal() }
}

plugins {
    id("de.fayard.refreshVersions")
}
