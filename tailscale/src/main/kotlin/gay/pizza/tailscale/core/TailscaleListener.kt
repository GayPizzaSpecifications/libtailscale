package gay.pizza.tailscale.core

import gay.pizza.tailscale.lib.TailscaleConnHandleOut
import gay.pizza.tailscale.lib.TailscaleListenerHandle

class TailscaleListener(private val tailscale: Tailscale, private val handle: TailscaleListenerHandle) {
  fun accept(): TailscaleConn {
    val out = TailscaleConnHandleOut()
    tailscale.check(tailscale.lib.tailscale_accept(handle, out))
    return TailscaleConn(tailscale, out.value)
  }

  fun acceptLoop(until: () -> Boolean = { true }, handler: (TailscaleConn) -> Unit) {
    while (until()) {
      handler(accept())
    }
  }

  fun threadedAcceptLoop(until: () -> Boolean = { true }, handler: (TailscaleConn) -> Unit) {
    while (until()) {
      val conn = accept()
      val thread = Thread {
        handler(conn)
      }
      thread.start()
    }
  }

  fun close() {
    tailscale.check(tailscale.lib.tailscale_listener_close(handle))
  }
}
