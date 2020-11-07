import java.util.Properties

plugins {
  `detekt-config`
  id("org.jetbrains.dokka")
  id("com.vanniktech.maven.publish") apply false
}

allprojects {
  repositories {
    google()
    mavenCentral()
    jcenter()
  }
}

val versionProperties = Properties().apply {
  load(file("versions.properties").inputStream())
}
val kotlinVersion by extra(versionProperties["version.kotlin"])
val composeVersion by extra(versionProperties["version.androidx.compose.ui"])

tasks.dokkaHtmlMultiModule.configure {
  outputDirectory.set(rootDir.resolve("docs/api"))
}
