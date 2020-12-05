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
    val kotlinVersion: String by rootProject.extra
    kotlinCompilerExtensionVersion = composeVersion
    kotlinCompilerVersion = kotlinVersion
  }
  lintOptions {
    // FIXME: Lint error with android gradle plugins >= 7.0.0
    disable("SyntheticAccessor", "ConvertToWebp", "GoogleAppIndexingWarning")
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
}
