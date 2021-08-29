import de.fayard.refreshVersions.core.versionFor

plugins {
  `module-config`
  com.android.library
  `kotlin-android`
}

android {
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.ui)
  }

  lint {
    disable += setOf("InvalidPackage")
  }
}

dependencies {
  implementation(Kotlin.stdlib.jdk8)
  implementation(AndroidX.appCompat)
  implementation(AndroidX.core.ktx)

  implementation(AndroidX.compose.ui)
  implementation(AndroidX.compose.material)

  implementation(AndroidX.test.coreKtx)
  implementation(AndroidX.test.runner)
  implementation(AndroidX.compose.ui.test)
  implementation(AndroidX.compose.ui.testJunit4)
}
