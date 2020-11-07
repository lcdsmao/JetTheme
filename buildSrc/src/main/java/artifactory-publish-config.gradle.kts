plugins {
  id("com.jfrog.artifactory")
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
