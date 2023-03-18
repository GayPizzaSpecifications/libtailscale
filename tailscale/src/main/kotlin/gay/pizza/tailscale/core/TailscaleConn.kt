package gay.pizza.tailscale.core

import com.sun.jna.platform.unix.LibC
import gay.pizza.tailscale.lib.TailscaleConnHandle
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class TailscaleConn(private val handle: TailscaleConnHandle) {
  private val file = File("/dev/fd/${handle}")

  fun inputStream(): InputStream = file.inputStream()
  fun outputStream(): OutputStream = file.outputStream()

  fun bufferedReader(): BufferedReader = inputStream().bufferedReader()
  fun bufferedWriter(): BufferedWriter = outputStream().bufferedWriter()

  fun close() {
    LibC.INSTANCE.close(handle)
  }
}
