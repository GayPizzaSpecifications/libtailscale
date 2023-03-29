package gay.pizza.tailscale.core

import com.sun.jna.platform.unix.LibC
import gay.pizza.tailscale.lib.TailscaleConnHandle
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.AsynchronousFileChannel
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

class TailscaleConn(tailscale: Tailscale, val handle: TailscaleConnHandle) {
  private val path = if (tailscale.useProcSelfFd) {
    Paths.get("/proc/self/fd/${handle}")
  } else {
    Paths.get("/dev/fd/${handle}")
  }

  fun inputStream(): InputStream = path.inputStream()
  fun outputStream(): OutputStream = path.outputStream()

  fun bufferedReader(): BufferedReader = inputStream().bufferedReader()
  fun bufferedWriter(): BufferedWriter = outputStream().bufferedWriter()

  fun openAsynchronousReadChannel(): AsynchronousFileChannel = AsynchronousFileChannel.open(
    path, StandardOpenOption.READ)
  fun openAsynchronousWriteChannel(): AsynchronousFileChannel = AsynchronousFileChannel.open(
    path, StandardOpenOption.READ)

  fun openReadChannel(): FileChannel = FileChannel.open(path, StandardOpenOption.READ)
  fun openWriteChannel(): FileChannel = FileChannel.open(path, StandardOpenOption.WRITE)

  fun close() {
    LibC.INSTANCE.close(handle)
  }
}
