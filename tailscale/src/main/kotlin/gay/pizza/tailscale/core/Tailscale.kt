package gay.pizza.tailscale.core

import gay.pizza.tailscale.lib.*

class Tailscale(internal val lib: LibTailscale = LibTailscaleLoader.load()) {
  private val handle: TailscaleHandle = lib.tailscale_new()

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
    return TailscaleConn(connHandle.value)
  }

  fun listen(network: String, addr: String): TailscaleListener {
    val listenerHandle = TailscaleListenerHandleOut()
    check(lib.tailscale_listen(handle, network, addr, listenerHandle))
    return TailscaleListener(this, listenerHandle.value)
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

  internal fun check(error: TailscaleError) {
    if (error == 0) return
    throw RuntimeException("Tailscale Error: Code: ${error}, Message: ${getLastErrorMessage()}")
  }

  companion object {
    fun configureAndUp(configure: Tailscale.() -> Unit = {}): Tailscale {
      val tailscale = Tailscale().apply(configure)
      tailscale.up()
      return tailscale
    }
  }
}
