import de.fayard.refreshVersions.core.versionFor
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `module-config`
  com.android.library
  `kotlin-android`
  id("com.vanniktech.maven.publish")
}

android {
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.ui)
  }
}

dependencies {
  api(project(":jettheme"))

  implementation(Kotlin.stdlib.jdk8)
  implementation(AndroidX.appCompat)
  implementation(AndroidX.core.ktx)

  implementation(AndroidX.compose.ui)
  implementation(AndroidX.compose.material)

  testImplementation(Testing.Kotest.runner.junit5)
  testImplementation(Testing.Kotest.assertions.core)

  androidTestImplementation(project(":testfixtures-android"))
  androidTestImplementation(AndroidX.test.coreKtx)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.compose.ui.test)
  androidTestImplementation(AndroidX.compose.ui.testJunit4)
  androidTestImplementation(Kotlin.Test.junit)
  androidTestUtil(AndroidX.test.orchestrator)
}

tasks.withType<KotlinCompile> {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}
