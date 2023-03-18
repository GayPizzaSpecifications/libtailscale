plugins {
  id("tailscale_base")
  `maven-publish`
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["kotlin"])
    }
  }

  repositories {
    mavenLocal()

    var githubPackagesToken = System.getenv("GITHUB_TOKEN")
    if (githubPackagesToken == null) {
      githubPackagesToken = project.findProperty("github.token") as String?
    }

    var gitlabPackagesToken = System.getenv("GITLAB_TOKEN")
    if (gitlabPackagesToken == null) {
      gitlabPackagesToken = project.findProperty("gitlab.com.accessToken") as String?
    }

    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/gaypizzaspecifications/libtailscale")
      credentials {
        username = project.findProperty("github.username") as String? ?: "gaypizzaspecifications"
        password = githubPackagesToken
      }
    }

    maven {
      name = "GitLab"
      url = uri("https://gitlab.com/api/v4/projects/44435887/packages/maven")
      credentials(HttpHeaderCredentials::class.java) {
        name = "Private-Token"
        value = gitlabPackagesToken
      }

      authentication {
        create<HttpHeaderAuthentication>("header")
      }
    }
  }
}
