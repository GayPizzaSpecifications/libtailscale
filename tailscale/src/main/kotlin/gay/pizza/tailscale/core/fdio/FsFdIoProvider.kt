package gay.pizza.tailscale.core.fdio

import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

class FsFdIoProvider(private val useProcSelfFd: Boolean = false) : FdIoProvider {
  override fun openInputStream(fd: Int): InputStream = pathForFd(fd).inputStream(StandardOpenOption.READ)
  override fun openOutputStream(fd: Int): OutputStream = pathForFd(fd).outputStream(StandardOpenOption.WRITE)

  override fun openReadChannel(fd: Int): ReadableByteChannel = FileChannel.open(pathForFd(fd), StandardOpenOption.READ)
  override fun openWriteChannel(fd: Int): WritableByteChannel = FileChannel.open(pathForFd(fd), StandardOpenOption.WRITE)

  private fun pathForFd(fd: Int): Path = if (useProcSelfFd) {
    Path("/proc/self/fd/${fd}")
  } else {
    Path("/dev/fd/${fd}")
  }
}
