rootProject.name = "libtailscale"

val topLevelModules = rootProject.projectDir.listFiles { item: File ->
  item.name.startsWith("tailscale-") &&
    item.isDirectory &&
    item.resolve("build.gradle.kts").exists()
}?.toList() ?: emptyList()

val externals = rootProject.projectDir.resolve("externals").listFiles { item: File ->
  item.isDirectory &&
    item.resolve("build.gradle.kts").exists()
}?.toList() ?: emptyList()

(topLevelModules + externals).map { file ->
  file.relativeTo(rootProject.projectDir)
}.forEach { file ->
  include(file.path.replace(File.separator, ":"))
}
