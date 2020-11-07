import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
  id("io.gitlab.arturbosch.detekt")
}

val analysisDir = file(projectDir)
val configFile = file("$rootDir/config/detekt/detekt.yml")

val kotlinFiles = "**/*.kt"
val kotlinScriptFiles = "**/*.kts"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"

subprojects {

  apply {
    plugin("io.gitlab.arturbosch.detekt")
  }

  tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
  }

  detekt {
    input = files(
      DetektExtension.DEFAULT_SRC_DIR_JAVA,
      "src/test/java",
      DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
      "src/test/kotlin"
    )
    config = files(configFile)
    buildUponDefaultConfig = true

    reports {
      xml.enabled = true
      html.enabled = true
      txt.enabled = true
    }
  }
}

allprojects {
  dependencies {
    detektPlugins(Libs.Detekt.formatting)
  }
}

val detektFormat by tasks.registering(Detekt::class) {
  description = "Formats whole project."
  parallel = true
  buildUponDefaultConfig = true
  autoCorrect = true
  ignoreFailures = true
  setSource(analysisDir)
  config.setFrom(configFile)
  include(kotlinFiles)
  include(kotlinScriptFiles)
  exclude(resourceFiles)
  exclude(buildFiles)
  reports {
    xml.enabled = false
    html.enabled = false
    txt.enabled = false
  }
}

val detektAll by tasks.registering(Detekt::class) {
  description = "Runs the whole project at once."
  parallel = true
  buildUponDefaultConfig = true
  setSource(analysisDir)
  config.setFrom(configFile)
  include(kotlinFiles)
  include(kotlinScriptFiles)
  exclude(resourceFiles)
  exclude(buildFiles)
  reports {
    xml.enabled = false
    html.enabled = false
    txt.enabled = false
  }
}
