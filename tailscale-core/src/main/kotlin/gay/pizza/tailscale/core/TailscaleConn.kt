package gay.pizza.tailscale.core

import gay.pizza.tailscale.lowlevel.TailscaleConnHandle

class TailscaleConn(private val tailscale: Tailscale, internal val handle: TailscaleConnHandle)
