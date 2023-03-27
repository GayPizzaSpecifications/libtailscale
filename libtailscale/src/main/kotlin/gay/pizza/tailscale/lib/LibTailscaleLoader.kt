package gay.pizza.tailscale.lib

import com.sun.jna.Native
import gay.pizza.tailscale.platforms.TailscalePlatformMarker
import java.nio.file.Files
import kotlin.io.path.absolutePathString
import kotlin.io.path.outputStream

object LibTailscaleLoader {
  private val osName = System.getProperty("os.name")?.lowercase() ?: "unknown"
  private val osArch = System.getProperty("os.arch")?.lowercase() ?: "unknown"

  private fun javaToGoOs(): String = when {
    osName.startsWith("windows") -> "windows"
    osName.startsWith("linux") -> "linux"
    osName.startsWith("mac") -> "darwin"
    else -> "unknown"
  }

  private fun goOsToSuffix(): String = when (goOs) {
    "linux" -> "so"
    "darwin" -> "dylib"
    "windows" -> "dll"
    else -> "so"
  }

  private fun javaToGoArch(): String = when {
    osArch.startsWith("amd64") -> "amd64"
    osArch.startsWith("x86_64") -> "amd64"
    osArch.startsWith("i386") -> "386"
    osArch.startsWith("x86") -> "386"
    osArch.startsWith("arm64") -> "arm64"
    osArch.startsWith("aarch64") -> "arm64"
    else -> "unknown"
  }

  private val goOs: String = javaToGoOs()
  private val goArch: String = javaToGoArch()
  private val libSuffix: String = goOsToSuffix()
  private val isUniversalSupported: Boolean = (goOs == "darwin") && when (goArch) {
    "amd64" -> true
    "arm64" -> true
    else -> false
  }

  private val resourceBasePath: String = when {
    isUniversalSupported -> "${goOs}/universal"
    else -> "${goOs}/${goArch}"
  }

  private val libraryResourcePath: String = "${resourceBasePath}/libtailscale.${libSuffix}"

  private fun extract(): String {
    val stream = TailscalePlatformMarker::class.java.getResourceAsStream(libraryResourcePath)
      ?: throw RuntimeException("Unable to load library $libraryResourcePath")
    val temporaryFile = Files.createTempFile("libtailscale", ".${libSuffix}")
    val output = temporaryFile.outputStream()
    stream.use { input ->
      output.use { out ->
        input.copyTo(out)
      }
    }
    return temporaryFile.absolutePathString()
  }

  private val path by lazy { extract() }

  fun load(): LibTailscale = Native.load(path, LibTailscale::class.java)
}
