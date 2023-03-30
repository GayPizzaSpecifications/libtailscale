package gay.pizza.tailscale.core

import com.sun.jna.platform.unix.LibC
import gay.pizza.tailscale.lib.TailscaleConnHandle
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

class TailscaleConn(val tailscale: Tailscale, val handle: TailscaleConnHandle) {
  fun inputStream(): InputStream = tailscale.fdIoProvider.openInputStream(handle)
  fun outputStream(): OutputStream = tailscale.fdIoProvider.openOutputStream(handle)

  fun bufferedReader(): BufferedReader = inputStream().bufferedReader()
  fun bufferedWriter(): BufferedWriter = outputStream().bufferedWriter()

  fun openReadChannel(): ReadableByteChannel = tailscale.fdIoProvider.openReadChannel(handle)
  fun openWriteChannel(): WritableByteChannel = tailscale.fdIoProvider.openWriteChannel(handle)

  fun close() {
    LibC.INSTANCE.close(handle)
  }
}
