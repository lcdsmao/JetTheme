import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `module-config`
  com.android.library
  `kotlin-android`
  id("com.vanniktech.maven.publish")
  `bintray-publish-config`
}

android {
  kotlinOptions {
    useIR = true
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    val composeVersion: String by rootProject.extra
    val kotlinVersion: String by rootProject.extra
    kotlinCompilerExtensionVersion = composeVersion
    kotlinCompilerVersion = kotlinVersion
  }

  testOptions {
    unitTests.all {
      it.useJUnitPlatform()
    }
  }
}

dependencies {
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.android)

  implementation(AndroidX.appCompat)
  implementation(AndroidX.core.ktx)
  implementation(Libs.AndroidX.DataStore.preferences)

  implementation(AndroidX.compose.runtime)
  implementation(AndroidX.compose.foundation)
  implementation(AndroidX.compose.animation)

  testImplementation(KotlinX.coroutines.test)
  testImplementation(Testing.Kotest.runner.junit5)
  testImplementation(Testing.Kotest.assertions.core)

  androidTestImplementation(Libs.AndroidX.Compose.uiTest)
}

tasks.withType<KotlinCompile> {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}
