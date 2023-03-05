package gay.pizza.tailscale.core

import com.sun.jna.Native
import gay.pizza.tailscale.lowlevel.*

class Tailscale(private val lib: LibTailscale = LibTailscaleLoader.load()) {
  private val handle: TailscaleHandle

  var hostname: String
    get() = throw UnsupportedOperationException("API does not support reading.")
    set(value) = check(lib.tailscale_set_hostname(handle, value))

  var authKey: String
    get() = throw UnsupportedOperationException("API does not support reading.")
    set(value) = check(lib.tailscale_set_authkey(handle, value))

  var controlUrl: String
    get() = throw UnsupportedOperationException("API does not support reading.")
    set(value) = check(lib.tailscale_set_control_url(handle, value))

  var directoryPath: String
    get() = throw UnsupportedOperationException("API does not support reading.")
    set(value) = check(lib.tailscale_set_dir(handle, value))

  var ephemeral: Boolean
    get() = throw UnsupportedOperationException("API does not support reading.")
    set(value) = check(lib.tailscale_set_ephemeral(handle, if (value) 0 else 1))

  init {
    handle = lib.tailscale_new()
  }

  fun start() {
    check(lib.tailscale_start(handle))
  }

  fun up() {
    check(lib.tailscale_up(handle))
  }

  fun close() {
    check(lib.tailscale_close(handle))
  }

  fun dial(network: String, addr: String): TailscaleConn {
    val connHandle = TailscaleConnHandleOut()
    check(lib.tailscale_dial(handle, network, addr, connHandle))
    return TailscaleConn(this, connHandle.value)
  }

  private fun getLastErrorMessage(): String {
    var buffer = CharArray(128)
    while (true) {
      if (buffer.size > 16384) {
        break
      }
      val code = lib.tailscale_errmsg(handle, buffer, buffer.size)
      if (code != 0) {
        buffer = CharArray(buffer.size * 2)
      }
    }
    return String(buffer)
  }

  private fun check(error: TailscaleError) {
    if (error == 0) return
    throw RuntimeException("Tailscale Error: Code: ${error}, Message: ${getLastErrorMessage()}")
  }
}
