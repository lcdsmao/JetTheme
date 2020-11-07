plugins {
  `module-config`
  com.android.application
  `kotlin-android`
  `kotlin-android-extensions`
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
}

dependencies {
  implementation(project(":theme-ambient"))

  implementation(Kotlin.stdlib.jdk8)
  implementation(AndroidX.appCompat)
  implementation(AndroidX.core.ktx)
  implementation(Libs.AndroidX.DataStore.preferences)

  implementation(AndroidX.compose.ui)
  implementation(AndroidX.compose.material)
}
