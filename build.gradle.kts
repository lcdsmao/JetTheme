import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
  `detekt-config`
  id("org.jetbrains.dokka")
  id("com.vanniktech.maven.publish") apply false
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

subprojects {
  val signingPropsFile = file("$rootDir/release/signing.properties")
    .takeIf { it.exists() } ?: return@subprojects

  loadProperties(signingPropsFile.path).forEach { (key, value) ->
    extra[key as String] = when (key) {
      "signing.secretKeyRingFile" -> {
        // If this is the key ring, treat it as a relative path
        rootProject.file(value).absolutePath
      }
      else -> value
    }
  }
}

tasks.dokkaHtmlMultiModule.configure {
  outputDirectory.set(rootDir.resolve("docs/api"))
}
