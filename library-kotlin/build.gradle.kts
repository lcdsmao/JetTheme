plugins {
  `module-config`
  `java-library`
  kotlin
  id("com.vanniktech.maven.publish")
  `bintray-publish-config`
}

dependencies {
  implementation(Kotlin.stdlib.jdk8)
}
