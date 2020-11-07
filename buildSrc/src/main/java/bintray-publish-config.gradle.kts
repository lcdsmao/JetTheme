import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Date

plugins {
  id("com.jfrog.bintray")
}

bintray {
  user = System.getProperty("bintrayUser")
  key = System.getProperty("bintrayKey")
  setPublications("maven")

  pkg(closureOf<BintrayExtension.PackageConfig> {
    repo = extra["BINTRAY_REPO"].toString()
    userOrg = extra["BINTRAY_USER_ORG"].toString()
    name = extra["POM_ARTIFACT_ID"].toString()
    vcsUrl = extra["POM_SCM_URL"].toString()
    websiteUrl = extra["POM_URL"].toString()
    setLicenses(extra["BINTRAY_LICENSE"].toString())
    version(closureOf<BintrayExtension.VersionConfig> {
      name = project.version.toString()
      released = Date().toString()
    })
  })
}

project.afterEvaluate {
  tasks.named("publishMavenPublicationToMavenLocal") {
    // Let bintray publication depend on artifact tasks created by 'com.vanniktech.maven.publish'
    tasks.getByName("publishMavenPublicationToLocalRepository").dependsOn.forEach {
      dependsOn(it)
    }
  }
}
