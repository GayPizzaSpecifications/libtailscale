package gay.pizza.tailscale.core

import gay.pizza.tailscale.lowlevel.TailscaleConnHandleOut
import gay.pizza.tailscale.lowlevel.TailscaleListenerHandle

class TailscaleListener(private val tailscale: Tailscale, private val handle: TailscaleListenerHandle) {
  fun accept(): TailscaleConn {
    val out = TailscaleConnHandleOut()
    tailscale.check(tailscale.lib.tailscale_accept(handle, out))
    return TailscaleConn(tailscale, out.value)
  }

  fun close() {
    tailscale.check(tailscale.lib.tailscale_listener_close(handle))
  }
}
