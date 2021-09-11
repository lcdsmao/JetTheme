import de.fayard.refreshVersions.core.versionFor

plugins {
  `module-config`
  com.android.application
  `kotlin-android`
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
  implementation(project(":jettheme-material"))

  implementation(Kotlin.stdlib.jdk8)
  implementation(AndroidX.appCompat)
  implementation(AndroidX.core.ktx)

  implementation(AndroidX.compose.ui)
  implementation(AndroidX.compose.material)
  implementation(AndroidX.navigation.compose)
  implementation(AndroidX.activity.compose)
}
