rootProject.name = "libtailscale"

val topLevelModules = rootProject.projectDir.listFiles { item: File ->
  item.name.contains("tailscale") &&
    item.isDirectory &&
    item.resolve("build.gradle.kts").exists()
}?.toList() ?: emptyList()

val externals = rootProject.projectDir.resolve("externals").listFiles { item: File ->
  item.isDirectory &&
    item.resolve("build.gradle.kts").exists()
}?.toList() ?: emptyList()

val examples = rootProject.projectDir.resolve("examples").listFiles { item: File ->
  item.isDirectory &&
    item.resolve("build.gradle.kts").exists()
}?.toList() ?: emptyList()

(topLevelModules + externals + examples).map { file ->
  file.relativeTo(rootProject.projectDir)
}.forEach { file ->
  include(file.path.replace(File.separator, ":"))
}
