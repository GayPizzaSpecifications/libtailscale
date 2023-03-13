plugins {
  tailscale_module
}

val release = "libtailscale-java-0.1.0"
val repository = "https://github.com/GayPizzaSpecifications/libtailscale-build"
val downloadAllUrl = "${repository}/releases/download/${release}/libtailscale-all.zip"
val downloadZipFile = project.file("${release}.zip")

val downloadPlatformLibraries = tasks.register("downloadPlatformLibraries") {
  outputs.file(downloadZipFile)

  doLast {
    if (downloadZipFile.exists()) {
      return@doLast
    }

    ant.withGroovyBuilder {
      "get"("src" to downloadAllUrl, "dest" to downloadZipFile.name)
    }
  }
}

val platformLibrariesPath = "src/main/resources/gay/pizza/tailscale/platforms"

val assemblePlatformLibraries = tasks.register("assemblePlatformLibraries") {
  dependsOn(downloadPlatformLibraries)

  doLast {
    project.file(platformLibrariesPath).deleteRecursively()
    zipTree(downloadZipFile).forEach { file ->
      val parts = file.name.split("-").map { it.split(".") }.flatten()
      val lib = parts[0]
      val platform = parts[1]
      val arch = parts[2]
      val suffix = parts[3]

      val outputFile = project.file("${platformLibrariesPath}/${platform}/${arch}/${lib}.${suffix}")
      outputFile.parentFile.mkdirs()
      file.copyTo(outputFile)
    }
  }
}

tasks.clean.get().doLast {
  downloadZipFile.delete()
  project.file(platformLibrariesPath).deleteRecursively()
}

tasks.processResources.get().dependsOn(assemblePlatformLibraries)
