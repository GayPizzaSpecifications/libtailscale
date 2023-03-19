package gay.pizza.tailscale.core

import com.sun.jna.platform.unix.LibC
import gay.pizza.tailscale.lib.TailscaleConnHandle
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousByteChannel
import java.nio.channels.AsynchronousFileChannel
import java.nio.channels.Channel
import java.nio.channels.CompletionHandler
import java.nio.channels.FileChannel
import java.nio.channels.SocketChannel
import java.nio.file.Paths
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

class TailscaleConn(val handle: TailscaleConnHandle) {
  private val path = Paths.get("/dev/fd/${handle}")

  fun inputStream(): InputStream = path.inputStream()
  fun outputStream(): OutputStream = path.outputStream()

  fun bufferedReader(): BufferedReader = inputStream().bufferedReader()
  fun bufferedWriter(): BufferedWriter = outputStream().bufferedWriter()

  fun openAsynchronousChannel(): AsynchronousFileChannel = AsynchronousFileChannel.open(path)
  fun openChannel(): FileChannel = FileChannel.open(path)

  fun close() {
    LibC.INSTANCE.close(handle)
  }
}
