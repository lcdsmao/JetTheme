plugins {
  `module-config`
  com.android.application
  `kotlin-android`
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
    kotlinCompilerExtensionVersion = composeVersion
  }
}

dependencies {
  implementation(project(":jettheme-material"))

  implementation(Kotlin.stdlib.jdk8)
  implementation(AndroidX.appCompat)
  implementation(AndroidX.core.ktx)

  implementation(AndroidX.compose.ui)
  implementation(AndroidX.compose.material)
  implementation(Libs.AndroidX.Compose.navigation)
  implementation(Libs.AndroidX.Compose.activity)
}
