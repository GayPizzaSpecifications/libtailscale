package gay.pizza.tailscale.core

import com.sun.jna.platform.unix.LibC
import gay.pizza.tailscale.lib.TailscaleConnHandle
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class TailscaleConn(private val tailscale: Tailscale, private val handle: TailscaleConnHandle) {
  private val file = File("/dev/fd/${handle}")

  fun inputStream(): InputStream = file.inputStream()
  fun outputStream(): OutputStream = file.outputStream()

  fun close() {
    LibC.INSTANCE.close(handle)
  }
}
