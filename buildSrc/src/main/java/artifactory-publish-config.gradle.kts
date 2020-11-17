import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

plugins {
  id("com.jfrog.artifactory")
  `maven-publish`
}

artifactory {
  setContextUrl("https://oss.jfrog.org/artifactory")
  withGroovyBuilder {
    "resolve" {
      "repository" {
        setProperty("repoKey", "libs-release")
        setProperty("username", System.getProperty("artifactoryUser"))
        setProperty("password", System.getProperty("artifactoryPassword"))
        setProperty("maven", true)
      }
    }

    "publish" {
      "repository" {
        if (project.version.toString().endsWith("-SNAPSHOT")) {
          setProperty("repoKey", "oss-snapshot-local")
        } else {
          setProperty("repoKey", "oss-release-local")
        }

        setProperty("username", System.getProperty("artifactoryUser"))
        setProperty("password", System.getProperty("artifactoryPassword"))
        setProperty("maven", true)
      }

      "defaults" {
        "publications"("maven")
      }
    }
  }
}

project.afterEvaluate {
  tasks.withType<ArtifactoryTask> {
    publishing.publications.forEach {
      val publishTaskName = "publish${it.name.capitalize()}PublicationToLocalRepository"
      dependsOn(publishTaskName)
    }
  }
}
