plugins {
  `kotlin-dsl`
}

repositories {
  jcenter()
  google()
}

dependencies {
  implementation(GradlePlugin.kotlin)
  implementation(GradlePlugin.android)
  implementation(GradlePlugin.detekt)
}

object GradlePlugin {
  const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:_"
  const val android = "com.android.tools.build:gradle:_"
  const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:_"
}
