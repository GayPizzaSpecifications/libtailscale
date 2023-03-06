plugins {
  tailscale_module
}

val release = "libtailscale-java-0.0.1"
val repository = "https://github.com/GayPizzaSpecifications/libtailscale-build"
val downloadAllUrl = "${repository}/releases/download/${release}/libtailscale-all.zip"

val downloadPlatformLibraries = tasks.register("downloadPlatformLibraries") {
  val zipFile = project.file("libtailscale-all.zip")
  outputs.file(zipFile)

  doLast {
    if (zipFile.exists()) {
      return@doLast
    }

    ant.withGroovyBuilder {
      "get"("src" to downloadAllUrl, "dest" to "libtailscale-all.zip")
    }
  }
}

val platformLibrariesPath = "src/main/resources/gay/pizza/tailscale/platforms"

val assemblePlatformLibraries = tasks.register("assemblePlatformLibraries") {
  dependsOn(downloadPlatformLibraries)

  doLast {
    project.file(platformLibrariesPath).deleteRecursively()
    zipTree("libtailscale-all.zip").forEach { file ->
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
  project.file("libtailscale-all.zip").delete()
  project.file("platformLibrariesPath").deleteRecursively()
}

tasks.processResources.get().dependsOn(assemblePlatformLibraries)
