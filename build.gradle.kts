import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
  `detekt-config`
  id("org.jetbrains.dokka")
  id("com.vanniktech.maven.publish")
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

  plugins.withId("com.vanniktech.maven.publish") {
    mavenPublish {
      sonatypeHost = SonatypeHost.S01
    }
  }
}

tasks.dokkaHtmlMultiModule.configure {
  outputDirectory.set(rootDir.resolve("docs/api"))
}
