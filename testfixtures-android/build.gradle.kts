plugins {
  `module-config`
  com.android.library
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
    disable("InvalidPackage")
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
  implementation(Libs.AndroidX.Compose.uiTest)
  implementation(Libs.AndroidX.Compose.uiTestJunit4)
}
